package br.com.vitorruiz.marvelapp.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.vitorruiz.marvelapp.data.repository.CharacterRepository
import br.com.vitorruiz.marvelapp.model.Character
import br.com.vitorruiz.marvelapp.model.Thumbnail
import br.com.vitorruiz.marvelapp.presentation.home.HomeViewModel
import br.com.vitorruiz.marvelapp.util.TestContextProvider
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class HomeViewModelTests {

    @get:Rule
    val instantTestExecutorRule = InstantTaskExecutorRule()

    private val testContextProvider = TestContextProvider()

    private lateinit var viewModel: HomeViewModel

    private val characterRepository: CharacterRepository = mockk(relaxed = true)
    private val favoriteResultObserver: Observer<Pair<Int, Character>> = mockk(relaxed = true)

    private val defaultCharacter = Character(
        1,
        "Iron Man",
        "cool description",
        Thumbnail("path", "extension")
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testContextProvider.testCoroutineDispatcher)
        viewModel = HomeViewModel(testContextProvider, characterRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testContextProvider.testCoroutineDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `Verify favoriteEvent observable`() {
        viewModel.favoriteResult().observeForever(favoriteResultObserver)

        Assert.assertTrue(viewModel.favoriteResult().hasObservers())
    }

    @Test
    fun `Should change value of favoriteResult on favoriteCharacter false called`() {
        viewModel.favoriteResult().observeForever(favoriteResultObserver)

        viewModel.favoriteCharacter(defaultCharacter, false, 1)
        testContextProvider.testCoroutineDispatcher.advanceUntilIdle()

        coVerify { characterRepository.removeFavorite(any()) }
        coVerify { favoriteResultObserver.onChanged(any()) }
    }

    @Test
    fun `Should change value of favoriteResult on favoriteCharacter true called`() {
        viewModel.favoriteResult().observeForever(favoriteResultObserver)

        viewModel.favoriteCharacter(defaultCharacter, true, 1)
        testContextProvider.testCoroutineDispatcher.advanceUntilIdle()

        coVerify { characterRepository.addFavorite(any()) }
        coVerify { favoriteResultObserver.onChanged(any()) }
    }
}