package se.ju.bookapp.Android

import se.ju.bookapp.Android.SearchResultModel.VolumeInfo

interface SearchResultClickListener {
    fun onItemClick(volumeInfo: VolumeInfo, bookId: String)
}