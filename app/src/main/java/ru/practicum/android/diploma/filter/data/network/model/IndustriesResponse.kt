package ru.practicum.android.diploma.filter.data.network.model

import ru.practicum.android.diploma.core.data.network.dto.Response
import ru.practicum.android.diploma.filter.data.dto.IndustryCategoryDto

data class IndustriesResponse(
    var industriesList: List<IndustryCategoryDto> = emptyList()
) : Response()
