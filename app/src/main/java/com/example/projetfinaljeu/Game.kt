package com.example.projetfinaljeu

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Game (
    val appid: Int,
    val header_image: String?,
    val background: String?,
    val background_raw: String?,
    val name: String?,
    val publishers: String?,
    val detailed_description: String?,
    val wishs: List<WishDetailGame>,
    val price_overview: String?,
    )  : Parcelable