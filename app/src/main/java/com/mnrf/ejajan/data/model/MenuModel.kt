package com.mnrf.ejajan.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuModel(
    val id: String,
    val name: String,
    val description: String,
    val ingredients: String,
    val preparationtime: String,
    val price: String,
    val imageurl: String,
) : Parcelable