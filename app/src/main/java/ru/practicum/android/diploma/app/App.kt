package ru.practicum.android.diploma.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.practicum.android.diploma.di.dataModule
import ru.practicum.android.diploma.di.interactorModule
import ru.practicum.android.diploma.di.repositoryModule
import ru.practicum.android.diploma.di.uiModule
import ru.practicum.android.diploma.di.viewModelModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(repositoryModule, interactorModule, viewModelModule, dataModule, uiModule)
        }
    }

}
