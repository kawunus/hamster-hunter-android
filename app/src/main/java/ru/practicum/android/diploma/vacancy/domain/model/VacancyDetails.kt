package ru.practicum.android.diploma.vacancy.domain.model

data class VacancyDetails(
    val id: String,
    val name: String,
    val company: String,
    val currency: String,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val area: String,
    val icon: String,
    val experience: String,//из obj со свойством name:String ->String
    val employmentForm: String?,
    val workFormat: String?,//array из obj со свойством name:String ->String
    val description: String,//html
    val keySkills: Array<String> //array из obj со свойством name:String
)
