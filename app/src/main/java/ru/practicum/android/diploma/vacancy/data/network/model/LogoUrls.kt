package ru.practicum.android.diploma.vacancy.data.network.model

import com.google.gson.annotations.SerializedName

data class LogoUrls(
    @SerializedName("original")
    val logoUrl: String?,
)
