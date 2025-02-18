package ru.practicum.android.diploma.util

import android.content.Context
import ru.practicum.android.diploma.R
import java.text.NumberFormat
import java.util.Locale

fun formatSalary(salaryFrom: Int?, salaryTo: Int?, currency: String, context: Context): String {
    return when {
        salaryFrom != null && salaryTo == null ->
            "${context.getString(R.string.from_title)} ${formatNumber(salaryFrom)} ${formatCurrency(currency)}"

        salaryFrom == null && salaryTo != null ->
            "${context.getString(R.string.to_title)} ${formatNumber(salaryTo)} ${formatCurrency(currency)}"

        salaryFrom != null && salaryTo != null ->
            "${context.getString(R.string.from_title)} ${formatNumber(salaryFrom)} " +
                "${context.getString(R.string.to)} ${formatNumber(salaryTo)} ${formatCurrency(currency)}"

        else -> context.getString(R.string.no_salary_info)
    }
}

fun formatNumber(number: Int): String {
    return NumberFormat.getInstance(Locale("RU")).format(number)
}

private fun formatCurrency(currency: String): String {
    return when (currency) {
        "RUR", "RUB" -> "₽"
        "BYR" -> "Br"
        "USD" -> "$"
        "EUR" -> "€"
        "KZT" -> "₸"
        "UAH" -> "₴"
        "AZN" -> "₼"
        "UZS" -> "Soʻm"
        "GEL" -> "₾"
        "KGS" -> "с"
        else -> {
            currency
        }
    }
}
