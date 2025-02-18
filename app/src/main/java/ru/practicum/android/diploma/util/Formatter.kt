package ru.practicum.android.diploma.util

import ru.practicum.android.diploma.R
import java.text.NumberFormat
import java.util.Locale

fun formatSalary(salaryFrom: Int?, salaryTo: Int?, currency: String): String {
    return when {
        salaryFrom != null && salaryTo == null ->
            "${R.string.from_title} ${formatNumber(salaryFrom)} ${formatCurrency(currency)}"

        salaryFrom == null && salaryTo != null ->
            "${R.string.to_title} ${formatNumber(salaryTo)} ${formatCurrency(currency)}"

        salaryFrom != null && salaryTo != null ->
            "${R.string.from_title} ${formatNumber(salaryFrom)} ${R.string.to} ${formatNumber(salaryTo)} ${
                formatCurrency(
                    currency
                )
            }"

        else -> "${R.string.no_salary_info}"
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
