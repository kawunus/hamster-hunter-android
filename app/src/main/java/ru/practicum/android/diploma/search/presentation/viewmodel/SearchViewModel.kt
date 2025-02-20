package ru.practicum.android.diploma.search.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
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
    private var searchState = MutableLiveData<SearchScreenState>(SearchScreenState.Default)
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
            // Запускаем getCount параллельно, оно подхватит только новые значения
            launch { getCount() }

            interactor.searchVacancies(expression)
                .cachedIn(viewModelScope)
                .catch { throwable ->
                    when (throwable) {
                        is EmptyResultException -> searchState.value = SearchScreenState.NothingFound
                        is NoInternetException -> searchState.value = SearchScreenState.NetworkError
                        else -> {
                            searchState.value = SearchScreenState.ServerError
                            Log.d("DEBUG", "ServerError: ${throwable.message}")
                        }
                    }
                }
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

sealed interface SearchScreenState {
    data object Default : SearchScreenState
    data object Loading : SearchScreenState
    sealed class Error : SearchScreenState
    data object ServerError : Error()
    data object NothingFound : Error()
    data object NetworkError : Error()
    data class SearchResults(val pagingData: PagingData<Vacancy>) : SearchScreenState
}
