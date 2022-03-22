package se.ju.bookapp.Android.SearchResultModel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageLinks(
    var thumbnail: String
): Parcelable