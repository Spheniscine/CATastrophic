package com.github.spheniscine.catastrophic

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.github.spheniscine.catastrophic.repository.CatRepository
import com.github.spheniscine.catastrophic.repository.CatRepositoryImpl
import com.github.spheniscine.catastrophic.repository.source.local.AppDatabase
import com.github.spheniscine.catastrophic.repository.source.local.CatPageDao
import com.github.spheniscine.catastrophic.repository.source.remote.CatApiService
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import timber.log.Timber

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        startKoin { modules(appModule()) }
    }
}

@Suppress("RemoveExplicitTypeArguments")
fun App.appModule(): Module = module {
    val app = this@appModule
    single<App> { app }
    single<Application> { app }
    single<Context> { app }

    single<CatApiService> { CatApiService.create() }

    single<AppDatabase> {
        Room.databaseBuilder(get(), AppDatabase::class.java, "catastrophic")
            .build()
    }
    single<CatPageDao> { get<AppDatabase>().catPageDao() }

    single<CatRepository> { CatRepositoryImpl(get(), get()) }

    viewModel { MainViewModel(get()) }
}