package se.ju.bookapp.Android.SearchResultModel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VolumeInfo(
    var authors: List<String>,
    var description: String,
    val imageLinks: ImageLinks,
    var pageCount: Int,
    var title: String
): Parcelable