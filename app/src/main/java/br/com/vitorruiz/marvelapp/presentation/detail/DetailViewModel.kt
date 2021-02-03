package br.com.vitorruiz.marvelapp.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.vitorruiz.marvelapp.core.CoroutineContextProvider
import br.com.vitorruiz.marvelapp.core.SingleLiveEvent
import br.com.vitorruiz.marvelapp.data.repository.CharacterRepository
import br.com.vitorruiz.marvelapp.model.Character
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailViewModel(
    private val contextProvider: CoroutineContextProvider,
    private val characterRepository: CharacterRepository
) : ViewModel() {

    private val mFavoriteEventLiveData = SingleLiveEvent<Character>()

    fun favoriteEvent() = mFavoriteEventLiveData

    fun favoriteCharacter(character: Character, favorite: Boolean) {
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
            mFavoriteEventLiveData.postValue(character)
        }
    }
}