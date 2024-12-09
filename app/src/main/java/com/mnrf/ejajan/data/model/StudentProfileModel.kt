package com.mnrf.ejajan.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StudentProfileModel(
    val id: String = "",
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val balance: String = "",
    val parent_uid: String = "",
    val password: String = "",
    val facecontour: String = ""
) : Parcelable
