package br.com.vitorruiz.marvelapp.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.vitorruiz.marvelapp.data.source.local.CharacterDao
import br.com.vitorruiz.marvelapp.data.source.network.ApiClient
import br.com.vitorruiz.marvelapp.data.repository.CharacterRepository
import br.com.vitorruiz.marvelapp.model.Character
import br.com.vitorruiz.marvelapp.model.Thumbnail
import br.com.vitorruiz.marvelapp.util.TestContextProvider
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CharacterRepositoryTests {

    @get:Rule
    val instantTestExecutorRule = InstantTaskExecutorRule()

    private val testContextProvider = TestContextProvider()

    private lateinit var characterRepository: CharacterRepository

    private val apiClient: ApiClient = mockk(relaxed = true)
    private val characterDao: CharacterDao = mockk(relaxed = true)

    private val defaultCharacter = Character(
        1,
        "Iron Man",
        "cool description",
        Thumbnail("path", "extension")
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testContextProvider.testCoroutineDispatcher)
        characterRepository = CharacterRepository(apiClient, characterDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testContextProvider.testCoroutineDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `Should add favorite`() = runBlockingTest {
        characterRepository.addFavorite(defaultCharacter)

        coVerify { characterDao.save(defaultCharacter) }
    }

    @Test
    fun `Should remove favorite`() = runBlockingTest {
        characterRepository.removeFavorite(defaultCharacter)

        coVerify { characterDao.delete(defaultCharacter.id) }
    }

    @Test
    fun `Should get all favorites`() = runBlockingTest {
        characterRepository.getFavoriteCharacters()

        coVerify { characterDao.getAll() }
    }

    @Test
    fun `Should get all favorites with LiveData`() {
        characterRepository.getFavoriteCharactersLiveData()

        verify { characterDao.getAllLive() }
    }

}