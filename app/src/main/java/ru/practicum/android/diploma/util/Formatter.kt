package ru.practicum.android.diploma.util

import android.content.Context
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.filter.domain.model.Area
import java.text.NumberFormat
import java.util.Locale

fun formatSalary(salaryFrom: Int?, salaryTo: Int?, currency: String, context: Context): String {
    return when {
        salaryFrom != null && salaryTo == null ->
            "${context.getString(R.string.from_title)} ${formatNumber(salaryFrom)} ${formatCurrency(currency, context)}"

        salaryFrom == null && salaryTo != null ->
            "${context.getString(R.string.to_title)} ${formatNumber(salaryTo)} ${formatCurrency(currency, context)}"

        salaryFrom != null && salaryTo != null ->
            "${context.getString(R.string.from_title)} ${formatNumber(salaryFrom)} " +
                "${context.getString(R.string.to)} ${formatNumber(salaryTo)} ${formatCurrency(currency, context)}"

        else -> context.getString(R.string.no_salary_info)
    }
}

fun formatNumber(number: Int): String {
    return NumberFormat.getInstance(Locale("RU")).format(number)
}

private fun formatCurrency(currency: String, context: Context): String {
    return when (currency) {
        "RUR", "RUB" -> context.getString(R.string.rur_rub)
        "BYR" -> context.getString(R.string.byr)
        "USD" -> context.getString(R.string.usd)
        "EUR" -> context.getString(R.string.eur)
        "KZT" -> context.getString(R.string.kzt)
        "UAH" -> context.getString(R.string.uah)
        "AZN" -> context.getString(R.string.azn)
        "UZS" -> context.getString(R.string.uzs)
        "GEL" -> context.getString(R.string.gel)
        "KGS" -> context.getString(R.string.kgt)
        else -> {
            currency
        }
    }
}

fun formatLocationString(area: Area?): String {
    return when {
        area?.country?.name.isNullOrEmpty() && area?.region?.name.isNullOrEmpty() -> ""
        area?.region?.name.isNullOrEmpty() -> area?.country?.name.orEmpty()
        else -> "${area?.country?.name}, ${area?.region?.name}"
    }
}
