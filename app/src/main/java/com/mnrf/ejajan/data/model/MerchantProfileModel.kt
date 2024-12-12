package com.mnrf.ejajan.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MerchantProfileModel(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val balance: String = "",
    val daysopen: String = "",
    val start1: String = "",
    val start2: String = "",
    val start3: String = "",
    val start4: String = "",
    val start5: String = "",
    val start6: String = "",
    val start7: String = "",
    val end1: String = "",
    val end2: String = "",
    val end3: String = "",
    val end4: String = "",
    val end5: String = "",
    val end6: String = "",
    val end7: String = ""
) : Parcelable


