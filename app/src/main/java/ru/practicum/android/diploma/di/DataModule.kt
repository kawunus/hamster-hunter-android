package ru.practicum.android.diploma.di

import android.content.Context
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.core.data.database.AppDatabase
import ru.practicum.android.diploma.core.data.network.HHApiService
import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.core.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.search.data.dto.VacanciesSearchRequest
import ru.practicum.android.diploma.search.data.impl.VacanciesPagingSource

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
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "hamster_hunter_database.db").build()
    }

    single {
        androidContext().getSharedPreferences("hamster_hunter_shared_preferences", Context.MODE_PRIVATE)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(
            context = androidContext(),
            hHApiService = get()
        )
    }

    factory { (searchRequest: VacanciesSearchRequest) ->
        VacanciesPagingSource(get(), searchRequest) //
    }

}
