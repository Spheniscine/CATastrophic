package com.example.catastrophic

import android.app.Application
import android.content.Context
import com.example.catastrophic.repository.CatRepository
import com.example.catastrophic.repository.source.CatApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class App: Application() {
    val app get() = this

    @Suppress("RemoveExplicitTypeArguments")
    fun appModule(): Module = module {
        single<App> { app }
        single<Application> { app }
        single<Context> { app }

        single<CatApiService> { CatApiService.create() }

        single<CatRepository> { CatRepository(get()) }

        viewModel { MainViewModel(get()) }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin { modules(appModule()) }
    }
}