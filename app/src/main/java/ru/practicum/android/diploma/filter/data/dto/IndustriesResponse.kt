package ru.practicum.android.diploma.filter.data.dto

import ru.practicum.android.diploma.core.data.dto.Response

class IndustriesResponse(
    var industriesList: List<IndustryCategoryDto> = emptyList()
) : Response()
