package br.com.vitorruiz.marvelapp.model

data class ApiResponseDataWrapper<T>(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<T>
) {
    fun totalPages() = if (total % limit == 0) total / limit else (total / limit) + 1
}