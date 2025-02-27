package ru.practicum.android.diploma.search.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
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
import ru.practicum.android.diploma.filter.domain.usecase.FiltersInteractor
import ru.practicum.android.diploma.search.domain.model.Vacancy
import ru.practicum.android.diploma.search.domain.usecase.VacanciesSearchInteractor
import ru.practicum.android.diploma.util.Constants.EMPTY_STRING
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(
    private val searchInteractor: VacanciesSearchInteractor,
    private val filtersInteractor: FiltersInteractor,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    private val searchState = MutableLiveData<SearchScreenState>(SearchScreenState.Default)
    fun getSearchState(): LiveData<SearchScreenState> = searchState

    private val foundCount = MutableLiveData<Int?>()
    fun getFoundCount(): LiveData<Int?> = foundCount

    private val anyFilterApplied = MutableLiveData<Boolean?>() // флаг для управления подсветкой кнопкой "фильтры"
    fun getAnyFilterApplied(): LiveData<Boolean?> = anyFilterApplied

    private val pagingDataLiveData = MutableLiveData<PagingData<Vacancy>>(PagingData.empty())
    fun getPagingDataLiveData(): LiveData<PagingData<Vacancy>> = pagingDataLiveData

    private val _searchText = MutableLiveData<String>()
    private val searchText: LiveData<String> get() = _searchText
    fun updateSearchText(searchText: String) {
        _searchText.value = searchText
    }

    fun getSearchText(): String = searchText.value ?: ""

    private var latestSearchText: String
        get() = savedStateHandle.get<String>(LATEST_SEARCH_TEXT) ?: EMPTY_STRING
        set(value) {
            savedStateHandle[LATEST_SEARCH_TEXT] = value
        }

    private val searchDebounce = debounce<String>(
        SEARCH_DEBOUNCE_DELAY,
        viewModelScope,
        useLastParam = true
    ) { expression ->
        startSearch(expression)
    }

    init {
        checkIfAnyFilterApplied()
    }

    fun searchWithDebounce(changedText: String) {
        val shouldSearchStart = shouldSearchStart(changedText)
        if (shouldSearchStart) {
            // Показываем загрузку
            searchState.postValue(SearchScreenState.Loading)
            // Запускаем отложенное выполнения поиска
            searchDebounce.invoke(changedText)
        }
    }

    fun checkIfAnyFilterApplied() {
        viewModelScope.launch {
            anyFilterApplied.value = filtersInteractor.checkIfAnyFilterApplied()
        }
    }

    fun startSearch(expression: String) {
        viewModelScope.launch {
            // Очищаем старые данные
            pagingDataLiveData.postValue(PagingData.empty())
            foundCount.postValue(null)

            // Загружаем общее количество найденных вакансий по запросу
            launch {
                getCount()
            }

            searchInteractor.searchVacancies(expression)
                .cachedIn(viewModelScope)
                .distinctUntilChanged() // Игнорировать повторные значения
                .collectLatest { data ->
                    pagingDataLiveData.postValue(data)
                    searchState.postValue(SearchScreenState.SearchResults)
                }
        }
    }

    private suspend fun getCount() {
        searchInteractor.foundCount
            .filterNotNull()
            .distinctUntilChanged() // Игнорировать повторные значения
            .collectLatest { count ->
                foundCount.postValue(count)
            }
    }

    private fun shouldSearchStart(changedText: String): Boolean {
        // Проверяем, был ли изменён  запрос с момента прошлого поиска
        if (latestSearchText == changedText && searchState.value is SearchScreenState.SearchResults) {
            return false // Если старый текст идентичен новому, не запускаем поиск повторно
        } else {
            latestSearchText = changedText
            return true
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
        private const val LATEST_SEARCH_TEXT = "latestSearchText"
    }
}
