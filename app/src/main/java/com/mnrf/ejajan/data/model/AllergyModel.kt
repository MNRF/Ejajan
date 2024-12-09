package com.mnrf.ejajan.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AllergyModel (
    val name: String = "",
    val student_uid: String = ""
) : Parcelable