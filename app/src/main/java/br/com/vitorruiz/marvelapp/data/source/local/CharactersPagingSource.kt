package br.com.vitorruiz.marvelapp.data.source.local

import androidx.paging.PagingSource
import br.com.vitorruiz.marvelapp.data.source.network.ApiClient
import br.com.vitorruiz.marvelapp.model.ApiResponseWrapper
import br.com.vitorruiz.marvelapp.model.Character

class CharactersPagingSource(
    private val apiClient: ApiClient,
    private val favoriteList: List<Character>,
    private val characterName: String?
) :
    PagingSource<Int, Character>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            with(
                apiClient.getApi().getCharacters(
                    name = characterName,
                    limit = PAGE_SIZE,
                    offset = position * PAGE_SIZE
                )
            ) { toLoadResult(this, position) }
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    private fun toLoadResult(
        data: ApiResponseWrapper<Character>,
        position: Int
    ): LoadResult<Int, Character> {
        return LoadResult.Page(
            data = data.data.results.onEach { remote ->
                remote.isFavorite = favoriteList.any { it.id == remote.id }
            },
            prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
            nextKey = if (position == data.data.totalPages()) null else position + 1
        )
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 0
        const val PAGE_SIZE = 20
    }
}