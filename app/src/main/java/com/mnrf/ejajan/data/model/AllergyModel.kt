package com.mnrf.ejajan.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AllergyModel (
    val name: String = "",
    val parentUid: String = ""
) : Parcelable