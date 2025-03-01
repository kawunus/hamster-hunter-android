package ru.practicum.android.diploma.filter.domain.model

import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.Region

data class Area(
    val country: Country?,
    val region: Region?
)
