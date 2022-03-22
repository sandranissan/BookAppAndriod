package se.ju.bookapp.Android

import se.ju.bookapp.Android.BookListModel.ListVolumeInfo

interface BookListClickListener {
    fun onItemClick(listVolumeInfo: ListVolumeInfo, bookId: String)

    fun onItemLongClick(listVolumeInfo: ListVolumeInfo, bookId: String)
}