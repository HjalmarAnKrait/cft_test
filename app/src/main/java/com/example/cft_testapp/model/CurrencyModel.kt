package com.example.cft_testapp.model


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

data class CurrencyModel(
    @Json(name = "CharCode")
    val charCode: String,
    @Json(name = "ID")
    val id: String,
    @Json(name = "Name")
    val name: String,
    @Json(name = "Nominal")
    val nominal: Int,
    @Json(name = "NumCode")
    val numCode: String,
    @Json(name = "Previous")
    val previous: Double,
    @Json(name = "Value")
    val value: Double
) : Serializable