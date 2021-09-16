package com.example.cft_testapp.networking

import android.util.Log
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DownloadService(val listener: (JSONObject) -> Unit) {
    fun getJson(){
        RepositoryProvider.provideRepository().apiService.getCurrency().enqueue(object:
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.e("432", "success" + response.body()?.toString())
                var responseString = response.body()?.string().toString()
                listener.invoke(JSONObject(responseString))

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }

        })
    }

}