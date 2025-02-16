package ru.practicum.android.diploma.di

import android.content.Context
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.core.data.database.AppDatabase
import ru.practicum.android.diploma.core.network.HHApiService
import ru.practicum.android.diploma.core.network.NetworkClient
import ru.practicum.android.diploma.core.network.RetrofitNetworkClient

val dataModule = module {

    single<HHApiService> {
        val hhBaseUrl = " "
        Retrofit.Builder()
            .baseUrl(hhBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HHApiService::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "hamster_hunter_database.db").build()
    }

    single {
        androidContext().getSharedPreferences("hamster_hunter_shared_preferences", Context.MODE_PRIVATE)
    }
}
