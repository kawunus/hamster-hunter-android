package ru.practicum.android.diploma.filter.data.network.model

import com.google.gson.annotations.SerializedName

data class RegionsRequest(
    @SerializedName("area_id")
    val countryId: String
)
