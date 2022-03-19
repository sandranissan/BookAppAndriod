package se.ju.bookapp.Android.SearchResultModel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VolumeInfo(
    val authors: List<String>,
    val description: String,
    val imageLinks: ImageLinks,
    val language: String,
    val pageCount: Int,
    val publishedDate: String,
    val publisher: String,
    val subtitle: String,
    val title: String
): Parcelable