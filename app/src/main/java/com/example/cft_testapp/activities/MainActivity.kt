package com.example.cft_testapp.activities

import android.app.Dialog
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cft_testapp.R
import com.example.cft_testapp.adapters.CurrencyAdapter
import com.example.cft_testapp.databinding.ConverterDialogBinding
import com.example.cft_testapp.model.CurrencyModel
import com.example.cft_testapp.networking.DownloadService
import com.example.cft_testapp.utility.JsonParser
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.json.JSONArray
import org.json.JSONObject
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList

//Первый проект на котлине. Кошмар:)
class MainActivity : AppCompatActivity(), CurrencyAdapter.MyOnItemClickListener{

    var recyclerView: RecyclerView? = null
    var dateTextView: TextView? = null
    var currencyList: ArrayList<CurrencyModel>? = null
    var jsonObject: JSONObject? = null
    val BUNDLE_JSON_KEY = "CURRENCY_JSON"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        dateTextView!!.visibility = View.GONE
        if(savedInstanceState?.getString((BUNDLE_JSON_KEY)) != null){
            fillRecyclerView(JSONObject(savedInstanceState.getString((BUNDLE_JSON_KEY))))
        }
        else{
            DownloadService{ result -> onResponse(result)}.getJson()
        }




    }



    override fun onSaveInstanceState(outState: Bundle) {
        val jsonListString = JSONArray(currencyList).toString()
        if(jsonObject != null){
            outState.putString(BUNDLE_JSON_KEY, jsonObject.toString())
        }

        super.onSaveInstanceState(outState)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        //jsonObject = JSONObject(savedInstanceState.getString((BUNDLE_JSON_KEY)))
    }

    private fun onResponse(jsonObject: JSONObject){
        this.jsonObject = jsonObject
        fillRecyclerView(jsonObject)
        dateTextView!!.visibility = View.VISIBLE
    }

    private fun initViews(){
        recyclerView = findViewById(R.id.recyclerView)
        dateTextView = findViewById(R.id.dateTextView)
    }

    private fun fillRecyclerView(jsonObject: JSONObject){
        val parser = JsonParser(jsonObject)
        val divider = DividerItemDecoration(this,LinearLayoutManager(this).orientation)

        dateTextView!!.setText("Курс на " + parser.getStringDate())
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView!!.addItemDecoration(divider)
        currencyList = ArrayList(parser.getCurrencyListOptional().get())
        recyclerView!!.adapter = CurrencyAdapter(currencyList!!, this)

    }

    override fun onItemClick(position: Int) {
        val currency = currencyList!!.get(position)
        showConverterDialog(currency)
    }

    private fun showConverterDialog(currency: CurrencyModel) {
        val dialog = Dialog(this)
        val dialogBinding: ConverterDialogBinding = ConverterDialogBinding.inflate(LayoutInflater.from(this))

        dialogBinding.currency = currency

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(dialogBinding.root)
        dialog.setCancelable(true)
        dialog.window!!.attributes.width = WindowManager.LayoutParams.FILL_PARENT

        dialogBinding.tipTextView.visibility = View.GONE
        val onClickListener = View.OnClickListener(){
            if(dialogBinding.toConvertEditText.text.isEmpty()){
                dialogBinding.tipTextView.visibility = View.VISIBLE
            }
            else{
                var toConvert: Double = dialogBinding.toConvertEditText.text.toString().toDouble()
                var result: BigDecimal = convertToRub(toConvert, currency)
                if(result != BigDecimal(0.0)){
                    dialogBinding.resultTextView.text = result.toString()
                    dialogBinding.tipTextView.visibility = View.GONE
                }
                else{
                    dialogBinding.tipTextView.visibility = View.VISIBLE
                }
            }
        }

        dialogBinding.nameTextView.text = "${currency.name}(${currency.charCode})"
        dialogBinding.dateTextView.text = dateTextView!!.text
        dialogBinding.exitDialogButton.setOnClickListener {dialog.dismiss()}
        dialogBinding.convertButton.setOnClickListener(onClickListener)

        dialog.show()
    }

    private fun convertToRub(toConvert: Double, currency: CurrencyModel): BigDecimal{
        var result: BigDecimal? = null


        if(toConvert % currency.nominal.toDouble() == 0.0){
            result = (BigDecimal(currency.value * toConvert) / BigDecimal(currency.nominal))
        }

        return result ?: BigDecimal(0.0)

    }

}