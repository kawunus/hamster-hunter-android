package ru.practicum.android.diploma.filter.data.dto

import com.google.gson.annotations.SerializedName

data class AreaDto(
    val id: String,
    @SerializedName("parent_id")
    val parentId: String?,
    val name: String?,
)
