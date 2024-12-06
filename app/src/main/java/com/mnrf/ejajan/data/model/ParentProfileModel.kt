package com.mnrf.ejajan.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParentProfileModel(
    val id: String = "",
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val balance: String = ""
) : Parcelable
