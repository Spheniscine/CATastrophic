package com.example.catastrophic

import android.app.Application
import android.content.Context
import com.example.catastrophic.ui.main.MainViewModel
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

        viewModel { MainViewModel() }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin { modules(appModule()) }
    }
}