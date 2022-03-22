package se.ju.bookapp.Android.BookListModel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ListImageLinks(
    var thumbnail: String? = null
): Parcelable