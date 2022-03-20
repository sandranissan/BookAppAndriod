package se.ju.bookapp.Android.BookListModel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import se.ju.bookapp.Android.SearchResultModel.VolumeInfo

@Parcelize
data class ListVolumeInfo(
    var authors: List<String>? = null,
    var description: String? = null,
    var imageLinks: ListImageLinks? = null,
    var pageCount: Int? = null,
    var title: String? = null
): Parcelable