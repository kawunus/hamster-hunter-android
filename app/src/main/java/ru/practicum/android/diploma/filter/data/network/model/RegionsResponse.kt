package ru.practicum.android.diploma.filter.data.network.model

import ru.practicum.android.diploma.core.data.network.dto.Response
import ru.practicum.android.diploma.filter.data.dto.RegionDto

class RegionsResponse : Response() {
    var regionsList: List<RegionDto> = emptyList()
}
