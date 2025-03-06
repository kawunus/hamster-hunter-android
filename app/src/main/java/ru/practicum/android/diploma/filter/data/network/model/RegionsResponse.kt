package ru.practicum.android.diploma.filter.data.network.model

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.core.data.network.dto.Response
import ru.practicum.android.diploma.filter.data.dto.RegionDto

class RegionsResponse : Response() {
    @SerializedName("areas")
    var regionsList: List<RegionDto> = emptyList()
}
