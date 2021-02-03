package br.com.vitorruiz.marvelapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import br.com.vitorruiz.marvelapp.data.source.local.CharacterDao
import br.com.vitorruiz.marvelapp.data.source.local.CharactersPagingSource
import br.com.vitorruiz.marvelapp.data.source.network.ApiClient
import br.com.vitorruiz.marvelapp.model.Character
import kotlinx.coroutines.flow.Flow

class CharacterRepository(
    private val apiClient: ApiClient,
    private val characterDao: CharacterDao
) {

    fun getCharactersPaginated(
        name: String?,
        favoriteList: List<Character>
    ): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(
                pageSize = CharactersPagingSource.PAGE_SIZE,
                enablePlaceholders = false
            ), pagingSourceFactory = { CharactersPagingSource(apiClient, favoriteList, name) }
        ).flow
    }

    suspend fun addFavorite(character: Character) {
        return characterDao.save(character)
    }

    suspend fun removeFavorite(character: Character) {
        return characterDao.delete(character.id)
    }

    suspend fun getFavoriteCharacters(): List<Character> {
        return characterDao.getAll()
    }

    fun getFavoriteCharactersLiveData() = characterDao.getAllLive()
}