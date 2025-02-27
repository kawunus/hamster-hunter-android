package ru.practicum.android.diploma.filter.data.dto

import com.google.gson.annotations.SerializedName

data class RegionDto(
    val id: String,
    val name: String?,
    @SerializedName("parent_id")
    val parentId: String?
)
