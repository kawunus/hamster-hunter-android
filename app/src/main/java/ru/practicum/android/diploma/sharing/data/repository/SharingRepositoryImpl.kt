package ru.practicum.android.diploma.sharing.data.repository

import android.content.Context
import android.content.Intent
import ru.practicum.android.diploma.sharing.domain.api.SharingRepository

class SharingRepositoryImpl(private val context: Context) : SharingRepository {
    override fun openUrlShare(shareUrl: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareUrl)
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }
}
