package ru.practicum.android.diploma.filter.domain.model

import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.Region

//data class Area(
//    val countryId: String? = null,
//    val regionId: String? = null,
//    val countryName: String? = null,
//    var regionName: String? = null
//)

data class Area(
    var country: Country?,
    var region: Region?
)
