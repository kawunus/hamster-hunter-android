package ru.practicum.android.diploma.filter.data.dto

import com.google.gson.annotations.SerializedName

data class RegionsRequest(
    @SerializedName("area_id")
    val countryId: String
)
