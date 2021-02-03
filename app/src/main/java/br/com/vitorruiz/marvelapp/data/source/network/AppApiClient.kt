package br.com.vitorruiz.marvelapp.data.source.network

import br.com.vitorruiz.marvelapp.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AppApiClient : ApiClient {

    private val mGson = GsonBuilder().create()

    private val mHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    override fun getApi(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create(mGson))
            .client(mHttpClient)

        return retrofit.build().create(ApiService::class.java)
    }
}

class AuthInterceptor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalUrl = original.url()
        val url = originalUrl.newBuilder()
            .addQueryParameter("apikey", BuildConfig.API_KEY)
            .addQueryParameter("hash", BuildConfig.API_HASH)
            .addQueryParameter("ts", BuildConfig.API_TIMESTAMP)
            .build()
        val requestBuilder = original.newBuilder().url(url)
        return chain.proceed(requestBuilder.build())
    }
}