package ru.practicum.android.diploma.di

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.core.data.database.AppDatabase
import ru.practicum.android.diploma.core.data.network.HHApiService
import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.core.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.search.data.dto.VacanciesSearchRequest
import ru.practicum.android.diploma.search.data.network.VacanciesPagingSource
import ru.practicum.android.diploma.util.Constants.HH_DATABASE_NAME
import ru.practicum.android.diploma.util.Constants.HH_SHARED_PREFS_NAME

val dataModule = module {

    single<HHApiService> {
        val hhBaseUrl = "https://api.hh.ru/"
        Retrofit.Builder()
            .baseUrl(hhBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HHApiService::class.java)
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, HH_DATABASE_NAME).build()
    }

    single {
        androidContext().getSharedPreferences(HH_SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(
            context = androidContext(),
            hHApiService = get()
        )
    }

    factory { (searchRequest: VacanciesSearchRequest, foundCount: MutableSharedFlow<Int?>) ->
        VacanciesPagingSource(networkClient = get(), searchRequest = searchRequest, foundCount = foundCount)
    }

}

