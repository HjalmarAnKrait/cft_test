package com.example.cft_testapp.utility

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.cft_testapp.model.CurrencyModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class JsonParser(var jsonObject: JSONObject = JSONObject()) {
    private val format = SimpleDateFormat("yyyy-MM-dd")

    fun getCurrencyListOptional(): Optional<List<CurrencyModel>> {
        val jsonMap = jsonObject.getJSONObject("Valute")
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val type = Types.newParameterizedType(Map::class.java, String::class.java, CurrencyModel::class.java)
        val adapter = moshi.adapter<Map<String, CurrencyModel>>(type)
        return Optional.of(adapter.fromJson(jsonMap.toString())!!.toList().map {it.second})
    }


    fun getDate() : Optional<Date>{


        return Optional.of(format.parse(jsonObject.getString("Date")))
    }

    fun getStringDate() : String{
        var stringDate = "NULL"
        if(getDate().isPresent){
            return format.format(getDate().get())
        }
        return stringDate

    }

}