package ru.practicum.android.diploma.search.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.data.network.exception.EmptyResultException
import ru.practicum.android.diploma.core.data.network.exception.NoInternetException
import ru.practicum.android.diploma.core.ui.BaseViewModel
import ru.practicum.android.diploma.search.domain.api.VacanciesSearchInteractor
import ru.practicum.android.diploma.search.domain.model.Vacancy
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(val interactor: VacanciesSearchInteractor) : BaseViewModel() {
    private val searchState = MutableLiveData<SearchScreenState>(SearchScreenState.Default)
    fun getSearchState(): LiveData<SearchScreenState> = searchState

    private val foundCount = MutableLiveData<Int>()
    fun getFoundCount(): LiveData<Int> = foundCount

    private val pagingDataLiveData = MutableLiveData<PagingData<Vacancy>>(PagingData.empty())
    fun getPagingDataLiveData(): LiveData<PagingData<Vacancy>> = pagingDataLiveData

    private val searchDebounce = debounce<String>(
        SEARCH_DEBOUNCE_DELAY,
        viewModelScope,
        useLastParam = true
    ) { expression ->
        startSearch(expression)
    }

    fun searchWithDebounce(changedText: String) {
        searchDebounce.invoke(changedText)
    }

    fun startSearch(expression: String) {
        viewModelScope.launch {
            // Очищаем старые данные
            pagingDataLiveData.postValue(PagingData.empty())
            // Показываем загрузку
            searchState.postValue(SearchScreenState.Loading)
            // Загружаем общее количество найденных вакансий по запросу
            launch {
                getCount()
            }

            interactor.searchVacancies(expression)
                .cachedIn(viewModelScope)
                .distinctUntilChanged() // Игнорировать повторные значения
                .collectLatest { data ->
                    pagingDataLiveData.postValue(data)
                    searchState.postValue(SearchScreenState.SearchResults)
                }
        }
    }

    private suspend fun getCount() {
        interactor.foundCount
            .filterNotNull()
            .distinctUntilChanged() // Игнорировать повторные значения
            .collectLatest { count ->
                foundCount.postValue(count)
            }
    }

    fun cancelSearchDebounce() {
        searchDebounce.cancel()
    }

    fun setErrorScreenState(error: LoadState.Error) {
        searchState.value =
            when (error.error) {
                is EmptyResultException -> SearchScreenState.NothingFound
                is NoInternetException -> SearchScreenState.NetworkError
                else -> SearchScreenState.ServerError
            }
    }

    fun setDefaultScreen() {
        searchState.value = SearchScreenState.Default
    }

    private companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}


