package ru.practicum.android.diploma.filter.presentation.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.practicum.android.diploma.core.ui.BaseViewModel
import ru.practicum.android.diploma.filter.domain.model.Area

class WorkplaceViewModel : BaseViewModel() {

    private val _selectedArea = MutableStateFlow(Area())
    val selectedArea = _selectedArea.asStateFlow()

    fun setRegion(id: String, name: String) {
        _selectedArea.value = _selectedArea.value.copy(
            regionId = id,
            regionName = name
        )
    }
}
