package br.com.vitorruiz.marvelapp.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import br.com.vitorruiz.marvelapp.data.repository.CharacterRepository
import br.com.vitorruiz.marvelapp.model.Character
import br.com.vitorruiz.marvelapp.model.Thumbnail
import br.com.vitorruiz.marvelapp.presentation.favorites.FavoritesViewModel
import br.com.vitorruiz.marvelapp.util.TestContextProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class FavoritesViewModelTests {

    @get:Rule
    val instantTestExecutorRule = InstantTaskExecutorRule()

    private val testContextProvider = TestContextProvider()

    private lateinit var viewModel: FavoritesViewModel

    private val characterRepository: CharacterRepository = mockk(relaxed = true)
    private val favoriteListObserver: Observer<List<Character>> = mockk(relaxed = true)
    private val favoriteEventObserver: Observer<Character> = mockk(relaxed = true)

    private val defaultCharacter = Character(
        1,
        "Iron Man",
        "cool description",
        Thumbnail("path", "extension")
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testContextProvider.testCoroutineDispatcher)
        viewModel = FavoritesViewModel(testContextProvider, characterRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testContextProvider.testCoroutineDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `Verify favoritesList observable`() {
        coEvery { characterRepository.getFavoriteCharactersLiveData() } returns MutableLiveData(
            listOf(defaultCharacter)
        )

        viewModel.favoritesList().observeForever(favoriteListObserver)

        Assert.assertTrue(viewModel.favoritesList().hasObservers())
    }

    @Test
    fun `Verify favoriteEvent observable`() {
        viewModel.favoriteEvent().observeForever(favoriteEventObserver)

        Assert.assertTrue(viewModel.favoriteEvent().hasObservers())
    }

    @Test
    fun `Should change value of favoriteEvent on favoriteCharacter called`() {
        viewModel.favoriteEvent().observeForever(favoriteEventObserver)
        val favoriteCharacter = defaultCharacter.apply { isFavorite = true }

        viewModel.removeFavorite(favoriteCharacter)
        testContextProvider.testCoroutineDispatcher.advanceUntilIdle()

        coVerify { characterRepository.removeFavorite(any()) }
        coVerify { favoriteEventObserver.onChanged(any()) }
    }
}