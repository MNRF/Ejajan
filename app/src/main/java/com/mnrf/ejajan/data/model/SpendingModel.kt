package com.mnrf.ejajan.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SpendingModel(
    val amount: String = "",
    val period: String = "",
    val student_uid: String = ""
) : Parcelable