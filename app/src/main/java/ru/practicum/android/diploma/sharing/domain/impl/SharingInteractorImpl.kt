package ru.practicum.android.diploma.sharing.domain.impl

import ru.practicum.android.diploma.sharing.domain.api.SharingInteractor
import ru.practicum.android.diploma.sharing.domain.api.SharingRepository

class SharingInteractorImpl(private val repository: SharingRepository) : SharingInteractor {
    override fun openVacancyShare(vacancyId: String) {
        val shareUrl = SHAREPREFIX + vacancyId
        repository.openUrlShare(shareUrl)
    }

    companion object {
        const val SHAREPREFIX = "https://hh.ru/vacancy/"
    }
}
