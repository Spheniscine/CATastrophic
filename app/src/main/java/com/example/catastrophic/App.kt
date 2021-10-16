package com.example.catastrophic

import android.app.Application
import android.content.Context
import com.example.catastrophic.repository.CatApiRepository
import com.example.catastrophic.repository.MockCatApiRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

class App: Application() {
    val app get() = this

    fun appModule(): Module = module {
        single { app }
        single<Application> { app }
        single<Context> { app }

        single<CatApiRepository> { MockCatApiRepository() /* TODO: change */ }

        viewModel { MainViewModel(get()) }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin { modules(appModule()) }
    }
}