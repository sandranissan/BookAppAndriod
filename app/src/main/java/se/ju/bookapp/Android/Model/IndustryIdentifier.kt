package se.ju.bookapp.Android.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IndustryIdentifier(
    val identifier: String,
    val type: String
):Parcelable