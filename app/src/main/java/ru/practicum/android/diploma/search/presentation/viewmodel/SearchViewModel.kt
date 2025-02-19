package ru.practicum.android.diploma.search.presentation.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
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
    private var searchState = MutableLiveData<SearchScreenState>(SearchScreenState.Empty)
    fun getSearchState(): LiveData<SearchScreenState> = searchState

    private val _foundCount = MutableLiveData<Int>()
    fun getFoundCount(): LiveData<Int> = _foundCount

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
        } else trackSearchDebounce.invoke(changedText)
    }

    fun cancelSearchDebounce() {
        trackSearchDebounce.cancel()
    }

    fun startSearch(expression: String) {
        viewModelScope.launch {
            searchState.postValue(SearchScreenState.Loading)
            interactor.searchVacancies(expression)
                .cachedIn(viewModelScope)
                .catch { throwable ->
                    when (throwable) {
                        is EmptyResultException -> searchState.value = SearchScreenState.NothingFound
                        is NoInternetException -> searchState.value = SearchScreenState.NetworkError
                        else -> searchState.value = SearchScreenState.Error(throwable.message ?: "Неизвестная ошибка")
                    }
                }
                .collectLatest { data ->
                    searchState.value = SearchScreenState.SearchResults(
                        pagingData = data
                    )
                }

            interactor.foundCount
                .filterNotNull()
                .collect { count ->
                    _foundCount.value = count
                }
        }
    }

    private fun getActualSearchResults(changedText: String): PagingData<Vacancy>? {
        if ((latestSearchText == changedText) && (searchState.value is
                SearchScreenState.SearchResults)
        ) {
            return (searchState.value as SearchScreenState.SearchResults).pagingData
        } else {
            latestSearchText = changedText
            return null
        }
    }

    private companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}

sealed interface SearchScreenState {
    data object Loading : SearchScreenState
    data object Empty : SearchScreenState
    data object NothingFound : SearchScreenState
    data object NetworkError : SearchScreenState
    data class Error(var message: String) : SearchScreenState
    data class SearchResults(var pagingData: PagingData<Vacancy>) : SearchScreenState
}
