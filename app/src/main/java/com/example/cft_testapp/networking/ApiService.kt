package com.example.cft_testapp.networking

import com.squareup.moshi.Moshi
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("daily_json.js")
    fun getCurrency() : Call<ResponseBody>

    companion object Factory {
        val baseUrl = "https://www.cbr-xml-daily.ru/"
        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .build()
            return retrofit.create(ApiService::class.java);
        }
    }
}