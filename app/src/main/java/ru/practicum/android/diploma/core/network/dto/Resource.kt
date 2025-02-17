package ru.practicum.android.diploma.core.network.dto

sealed class Resource<T>(val data: T, val message: String? = null, val resultCode: Int) {
    class Success<T>(data: T, resultCode: Int) : Resource<T>(data = data, resultCode = resultCode)
    class Error<T>(message: String, data: T, resultCode: Int) :
        Resource<T>(data = data, message = message, resultCode = resultCode)
}
