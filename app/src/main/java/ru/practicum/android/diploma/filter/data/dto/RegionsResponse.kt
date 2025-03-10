package ru.practicum.android.diploma.filter.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.core.data.dto.Response

class RegionsResponse : Response() {
    @SerializedName("areas")
    var regionsList: List<RegionDto> = emptyList()
}
