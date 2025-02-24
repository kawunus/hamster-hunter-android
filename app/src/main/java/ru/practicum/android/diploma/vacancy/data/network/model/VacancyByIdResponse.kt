package ru.practicum.android.diploma.vacancy.data.network.model

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.core.data.network.dto.Response

data class VacancyByIdResponse(
    val name: String?,
    val salary: Salary?,
    val employer: Employer?,
    val area: Area?,
    val experience: Experience?,
    @SerializedName("employment_form")
    val employment: Employment?,
    @SerializedName("work_format")
    val workFormat: List<WorkFormat>?,
    val description: String?,
    @SerializedName("key_skills")
    val keySkills: List<KeySkills>?
) : Response()
