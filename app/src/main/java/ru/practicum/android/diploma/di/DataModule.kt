package ru.practicum.android.diploma.di

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.core.data.database.AppDatabase
import ru.practicum.android.diploma.core.data.network.HHApiService
import ru.practicum.android.diploma.core.data.network.NetworkClient
import ru.practicum.android.diploma.core.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.search.data.dto.VacanciesSearchRequest
import ru.practicum.android.diploma.search.data.network.VacanciesPagingSource
import ru.practicum.android.diploma.util.Constants.HH_BASE_URL
import ru.practicum.android.diploma.util.Constants.HH_DATABASE_NAME
import ru.practicum.android.diploma.util.Constants.HH_SHARED_PREFS_NAME

val dataModule = module {
// HHApiService
    single<HHApiService> {
        val hhBaseUrl = HH_BASE_URL
        Retrofit.Builder()
            .baseUrl(hhBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HHApiService::class.java)
    }
// AppDatabase
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, HH_DATABASE_NAME).build()
    }
// SharedPreferences
    single {
        androidContext().getSharedPreferences(HH_SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }
// NetworkClient
    singleOf(::RetrofitNetworkClient) { bind<NetworkClient>() }

// VacanciesPagingSource
    factory { (searchRequest: VacanciesSearchRequest, foundCount: MutableSharedFlow<Int?>) ->
        VacanciesPagingSource(networkClient = get(), searchRequest = searchRequest, foundCount = foundCount)
    }

}
