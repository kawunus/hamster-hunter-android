package ru.practicum.android.diploma.search.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.domain.exception.EmptyResultException
import ru.practicum.android.diploma.core.domain.exception.NoInternetException
import ru.practicum.android.diploma.core.ui.BaseViewModel
import ru.practicum.android.diploma.search.domain.api.VacanciesSearchInteractor
import ru.practicum.android.diploma.search.domain.model.Vacancy
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(val interactor: VacanciesSearchInteractor) : BaseViewModel() {
    private var latestSearchText = ""
    private val searchState = MutableLiveData<SearchScreenState>(SearchScreenState.Default)
    fun getSearchState(): LiveData<SearchScreenState> = searchState

    private val _foundCount = MutableLiveData<Int>()
    fun getFoundCount(): LiveData<Int> = _foundCount

    private val _isNextPageLoading = MutableLiveData(false)
    fun getIsNextPageLoading(): LiveData<Boolean> = _isNextPageLoading

    private val trackSearchDebounce = debounce<String>(
        SEARCH_DEBOUNCE_DELAY,
        viewModelScope,
        useLastParam = true
    ) { expression ->
        startSearch(expression)
    }

    fun searchWithDebounce(changedText: String) {
        val actualSearchResults = getActualSearchResults(changedText)
        if (actualSearchResults != null) {
            searchState.postValue(SearchScreenState.SearchResults(actualSearchResults))
            return
        } else {
            trackSearchDebounce.invoke(changedText)
        }
    }

    fun cancelSearchDebounce() {
        trackSearchDebounce.cancel()
    }

    fun startSearch(expression: String) {
        viewModelScope.launch {
            searchState.postValue(SearchScreenState.Loading)
            delay(SEARCH_DEBOUNCE_DELAY)
            // временно добавила задержку для упрощения тестирования progressbar.
            // УДАЛИТЬ ПОСЛЕ ОТЛАДКИ!!!

            launch { getCount() }
            interactor.searchVacancies(expression)
                .cachedIn(viewModelScope)
//                .catch { throwable ->
//                    processError(throwable)
//                }
                .collectLatest { data ->
                    searchState.value = SearchScreenState.SearchResults(
                        pagingData = data
                    )
                }
        }
    }

    private suspend fun getCount() {
        interactor.foundCount
            .filterNotNull()
            .collect { count ->
                _foundCount.value = count
            }
    }

    fun setNextPageLoading(isLoading: Boolean) {
        _isNextPageLoading.value = isLoading
    }

    fun setErrorState(error: LoadState.Error) {
        searchState.value =
            when (error.error) {
                is EmptyResultException -> SearchScreenState.NothingFound
                is NoInternetException -> SearchScreenState.NetworkError
                else -> SearchScreenState.ServerError
            }
    }

    private fun getActualSearchResults(changedText: String): PagingData<Vacancy>? {
        if (latestSearchText == changedText && searchState.value is SearchScreenState.SearchResults) {
            return (searchState.value as SearchScreenState.SearchResults).pagingData
        } else {
            latestSearchText = changedText
            return null
        }
    }

    fun setDefaultScreen() {
        searchState.value = SearchScreenState.Default
    }

    private companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}


