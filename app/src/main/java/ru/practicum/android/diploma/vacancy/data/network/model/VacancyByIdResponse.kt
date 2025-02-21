package ru.practicum.android.diploma.vacancy.data.network.model

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.core.data.network.dto.Response

data class VacancyByIdResponse(
    val id: String,
    val name: String,
    val salary: Salary,
    val employer: Employer,
    val area: Area,
    val experience: Experience,
    val employment: Employment,
    @SerializedName("work_format") val workFormat: WorkFormat,
    val description: String,
    @SerializedName("key_skills") val keySkills: String
) : Response()

