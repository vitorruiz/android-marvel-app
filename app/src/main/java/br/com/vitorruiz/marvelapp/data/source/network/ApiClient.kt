package br.com.vitorruiz.marvelapp.data.source.network

interface ApiClient {
    fun getApi(): ApiService
}