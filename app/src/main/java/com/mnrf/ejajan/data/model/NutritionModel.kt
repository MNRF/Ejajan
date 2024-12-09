package com.mnrf.ejajan.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NutritionModel (
    val name: String = "",
/*    val mineral: String = "",*/
    val student_uid: String = ""
) : Parcelable