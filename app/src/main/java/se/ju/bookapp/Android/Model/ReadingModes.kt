package se.ju.bookapp.Android.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReadingModes(
    val image: Boolean,
    val text: Boolean
): Parcelable