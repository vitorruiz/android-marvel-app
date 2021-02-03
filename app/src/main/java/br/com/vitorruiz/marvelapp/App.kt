package br.com.vitorruiz.marvelapp

import android.app.Application
import br.com.vitorruiz.marvelapp.core.CoroutineContextProvider
import br.com.vitorruiz.marvelapp.data.source.local.buildAppDatabase
import br.com.vitorruiz.marvelapp.data.source.network.ApiClient
import br.com.vitorruiz.marvelapp.data.source.network.AppApiClient
import br.com.vitorruiz.marvelapp.data.repository.CharacterRepository
import br.com.vitorruiz.marvelapp.presentation.detail.DetailViewModel
import br.com.vitorruiz.marvelapp.presentation.favorites.FavoritesViewModel
import br.com.vitorruiz.marvelapp.presentation.home.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(module {
                single<ApiClient> { AppApiClient() }
                single { CoroutineContextProvider() }
                single { buildAppDatabase(this@App).characterDao() }
                single { CharacterRepository(get(), get()) }
                viewModel { HomeViewModel(get(), get()) }
                viewModel { FavoritesViewModel(get(), get()) }
                viewModel { DetailViewModel(get(), get()) }
            })
        }
    }
}