package se.ju.bookapp.Android

import se.ju.bookapp.Android.Model.VolumeInfo

interface BookClickListener {
    fun onItemClick(volumeInfo: VolumeInfo, bookId: String)
}