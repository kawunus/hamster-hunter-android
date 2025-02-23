package ru.practicum.android.diploma.sharing.domain.api

interface SharingRepository {
    fun openUrlShare(shareUrl: String)
}
