package se.ju.bookapp.Android.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class PanelizationSummary(
    val containsEpubBubbles: Boolean,
    val containsImageBubbles: Boolean
): Parcelable