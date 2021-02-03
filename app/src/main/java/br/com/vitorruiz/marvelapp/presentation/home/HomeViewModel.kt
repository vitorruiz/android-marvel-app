package br.com.vitorruiz.marvelapp.presentation.home

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import br.com.vitorruiz.marvelapp.core.CoroutineContextProvider
import br.com.vitorruiz.marvelapp.core.SingleLiveEvent
import br.com.vitorruiz.marvelapp.data.repository.CharacterRepository
import br.com.vitorruiz.marvelapp.model.Character
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val contextProvider: CoroutineContextProvider,
    private val characterRepository: CharacterRepository
) : ViewModel(),
    LifecycleObserver {

    private val mPagingData = MutableLiveData<PagingData<Character>>()
    private val mFavoriteResult = SingleLiveEvent<Pair<Int, Character>>()
    var lastSearchedQuery: String? = null

    fun pagingData() = mPagingData
    fun favoriteResult() = mFavoriteResult

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        searchCharacters(null)
    }

    fun searchCharacters(name: String?) {
        lastSearchedQuery = name
        viewModelScope.launch {
            val favoriteList = async { characterRepository.getFavoriteCharacters() }.await()
            characterRepository.getCharactersPaginated(name, favoriteList)
                .cachedIn(this)
                .collect { mPagingData.value = it }
        }
    }

    fun favoriteCharacter(character: Character, favorite: Boolean, position: Int) {
        character.isFavorite = favorite
        viewModelScope.launch {
            if (favorite) {
                withContext(contextProvider.IO) {
                    characterRepository.addFavorite(character)
                }
            } else {
                withContext(contextProvider.IO) {
                    characterRepository.removeFavorite(character)
                }
            }
            mFavoriteResult.postValue(Pair(position, character))
        }
    }
}