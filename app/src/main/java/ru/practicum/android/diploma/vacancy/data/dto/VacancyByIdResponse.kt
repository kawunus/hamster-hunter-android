package ru.practicum.android.diploma.vacancy.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.core.data.dto.Response

data class VacancyByIdResponse(
    val id: String?,
    val name: String?,
    val salary: Salary?,
    val employer: Employer?,
    val area: Area?,
    val address: Address?,
    val experience: Experience?,
    @SerializedName("employment_form")
    val employment: Employment?,
    @SerializedName("work_format")
    val workFormat: List<WorkFormat>?,
    val description: String?,
    @SerializedName("key_skills")
    val keySkills: List<KeySkills>?,
    val icon: String?,
    @SerializedName("alternate_url")
    val alternateUrl: String?
) : Response()
