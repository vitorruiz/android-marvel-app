package br.com.vitorruiz.marvelapp.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.vitorruiz.marvelapp.core.CoroutineContextProvider
import br.com.vitorruiz.marvelapp.core.SingleLiveEvent
import br.com.vitorruiz.marvelapp.data.repository.CharacterRepository
import br.com.vitorruiz.marvelapp.model.Character
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesViewModel(
    private val contextProvider: CoroutineContextProvider,
    private val characterRepository: CharacterRepository
) : ViewModel() {

    private val mFavoriteEventLiveData = SingleLiveEvent<Character>()

    fun favoritesList() = characterRepository.getFavoriteCharactersLiveData()
    fun favoriteEvent() = mFavoriteEventLiveData

    fun removeFavorite(character: Character) {
        character.isFavorite = false
        viewModelScope.launch {
            withContext(contextProvider.IO) {
                characterRepository.removeFavorite(character)
                mFavoriteEventLiveData.postValue(character)
            }
        }
    }
}