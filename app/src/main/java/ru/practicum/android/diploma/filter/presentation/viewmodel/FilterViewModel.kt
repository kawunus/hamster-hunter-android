package ru.practicum.android.diploma.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.ui.BaseViewModel
import ru.practicum.android.diploma.filter.domain.api.FiltersInteractor
import ru.practicum.android.diploma.filter.domain.model.FilterParameters

class FilterViewModel(private val interactor: FiltersInteractor) : BaseViewModel() {
    init {
        checkSavedFilters() // получаем свежие данные о сохранённых фильтрах при инициализации ViewModel
    }
    // обязательно дополнительно в OnResume обновлять данные  (checkSavedFilters)

    private val savedFiltersLiveData = MutableLiveData<FilterParameters?>(null)
    fun getSavedFiltersLiveData(): LiveData<FilterParameters?> = savedFiltersLiveData

    private fun checkSavedFilters() {
        viewModelScope.launch {
            savedFiltersLiveData.value = interactor.read()
        }
    }

    private fun updateFilters(update: (FilterParameters) -> FilterParameters) {
        val currentFilters = savedFiltersLiveData.value
            ?: FilterParameters() // если сохранённых фильтров ещё не было - создаём обьект FilterParameters null-значениями
        val newFilters = update(currentFilters) // применеяем лямбду update к сохранённым ранеее фильтрам
        savedFiltersLiveData.value = newFilters

        viewModelScope.launch {
            interactor.save(newFilters)
        }  // сохраняем новые значения в корутине, чтобы не вызывать задержку интерфейса
    }

    fun clearFilters() {
        interactor.clear()
        savedFiltersLiveData.value = null
    }

    fun setArea(area: String) {
        updateFilters { it.copy(area = area) }
    }

    fun setProfessionalRole(role: String) {
        updateFilters { it.copy(professionalRole = role) }
    }

    fun setOnlyWithSalary(onlyWithSalary: Boolean) {
        updateFilters { it.copy(onlyWithSalary = onlyWithSalary) }
    }

    fun setOnlyInTitles(onlyInTitles: Boolean) {
        updateFilters { it.copy(onlyInTitles = onlyInTitles) }
    }
}

// Нажатие на кнопку «Применить» на экране фильтра возвращает пользователя на экран поиска.
// И если поле ввода поискового запроса было не пустым, то этот поисковый запрос должен
// автоматически выполниться повторно с применением актуальных настроек фильтрации.

// При нажатии на кнопку «Назад» пользователь возвращается на экран поиска, и актуальные настройки фильтра к последнему поисковому запросу не применяются автоматически.

// Если в приложении есть сохранённые непустые настройки параметров фильтрации, то кнопка фильтра на главном экране находится в подсвеченном состоянии.

// Кнопка «Применить» появляется, если пользователь указал фильтр, отличающийся от предыдущего.
// Нажатие на кнопку «Применить» приводит к сохранению выбранных настроек фильтра и применению фильтра для всех последующих запросов на поиск вакансий до изменения фильтра.
