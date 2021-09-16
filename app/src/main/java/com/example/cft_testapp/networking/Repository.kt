package com.example.cft_testapp.networking

import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call

class Repository(val apiService: ApiService) {
    fun getCurrency() : Call<ResponseBody>{
        return apiService.getCurrency()
    }

}