package ru.practicum.android.diploma.filter.domain.model

import java.io.Serializable

data class Region(
    val id: String,
    val name: String?,
    val parentId: String?
): Serializable
