package ru.practicum.android.diploma.search.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.domain.exception.EmptyResultException
import ru.practicum.android.diploma.core.domain.exception.NoInternetException
import ru.practicum.android.diploma.core.ui.BaseViewModel
import ru.practicum.android.diploma.search.domain.api.VacanciesSearchInteractor
import ru.practicum.android.diploma.search.domain.model.Vacancy
import ru.practicum.android.diploma.util.debounce

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModel(val interactor: VacanciesSearchInteractor) : BaseViewModel() {
    private var latestSearchText = ""
    private val searchState = MutableLiveData<SearchScreenState>(SearchScreenState.Default)
    fun getSearchState(): LiveData<SearchScreenState> = searchState

    private val _foundCount = MutableLiveData<Int>()
    fun getFoundCount(): LiveData<Int> = _foundCount

    private val _searchQuery = MutableStateFlow("")

    private val searchDebounce = debounce<String>(
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
            searchDebounce.invoke(changedText)
        }
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
                .collectLatest { data ->
                    searchState.value = SearchScreenState.SearchResults(data)
                }
        }
    }

//    fun startSearch(expression: String) {
//        viewModelScope.launch {
//            searchState.postValue(SearchScreenState.Loading)
//            delay(SEARCH_DEBOUNCE_DELAY)
//            // временно добавила задержку для упрощения тестирования progressbar.
//            // УДАЛИТЬ ПОСЛЕ ОТЛАДКИ!!!
//
//            launch { getCount() }
//            interactor.searchVacancies(expression)
//                .cachedIn(viewModelScope)
//                .collectLatest { data ->
//                    searchState.value = SearchScreenState.SearchResults(
//                        pagingData = data
//                    )
//                }
//        }
//    }

    //    val vacanciesFlow = _searchQuery
//        .flatMapLatest { query ->
//            if (query.isBlank()) {
//                flowOf(PagingData.empty())
//                //Добавить смену стейта????
//            } else {
//                interactor.searchVacancies(query)
//            }
//        }
//        .cachedIn(viewModelScope)

    private suspend fun getCount() {
        interactor.foundCount
            .filterNotNull()
            .collectLatest { count ->
                _foundCount.value = count
            }
    }

    fun cancelSearchDebounce() {
        searchDebounce.cancel()
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


