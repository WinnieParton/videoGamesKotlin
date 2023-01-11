package com.example.projetfinaljeu

import android.os.Parcelable
import android.provider.ContactsContract.CommonDataKinds.Email
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Login (
    val email: Email,
    val password: String
    )  : Parcelable