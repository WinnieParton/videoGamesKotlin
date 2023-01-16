package com.example.projetfinaljeu

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WishDetailGame (val steamid: String,
                           val num_games_owned: Int,
                           val votes_up: Int,
                           val review:String)  : Parcelable