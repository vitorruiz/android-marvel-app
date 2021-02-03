package br.com.vitorruiz.marvelapp.data.source.network

import br.com.vitorruiz.marvelapp.model.ApiResponseWrapper
import br.com.vitorruiz.marvelapp.model.Character
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("characters")
    suspend fun getCharacters(
        @Query("nameStartsWith") name: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null,
    ): ApiResponseWrapper<Character>
}