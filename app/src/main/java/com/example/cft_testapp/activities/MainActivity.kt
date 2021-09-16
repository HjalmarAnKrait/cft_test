package com.example.cft_testapp.activities

import android.app.Dialog
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cft_testapp.R
import com.example.cft_testapp.adapters.CurrencyAdapter
import com.example.cft_testapp.model.CurrencyModel
import com.example.cft_testapp.networking.DownloadService
import com.example.cft_testapp.utility.JsonParser
import org.json.JSONObject
import java.math.BigDecimal
import java.util.*

class MainActivity : AppCompatActivity(), CurrencyAdapter.MyOnItemClickListener{

    var recyclerView: RecyclerView? = null
    var dateTextView: TextView? = null
    var currencyList: List<CurrencyModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        dateTextView!!.visibility = View.GONE
        //Загрузка JSON-файла и обратный вызов метода onResponse в MainActivity
        DownloadService{ result -> onResponse(result)}.getJson()

    }

    private fun onResponse(jsonObject: JSONObject){
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
        currencyList = parser.getCurrencyListOptional().get()
        recyclerView!!.adapter = CurrencyAdapter(currencyList!!, this)

    }

    override fun onItemClick(position: Int) {
        val currency = currencyList!!.get(position)
        showConverterDialog(currency)
    }

    private fun showConverterDialog(currency: CurrencyModel) {
        val dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.converter_dialog)
        dialog.window!!.attributes.width = WindowManager.LayoutParams.FILL_PARENT

        val nameTextView: TextView = dialog.findViewById(R.id.nameTextView)
        val nominalTextView: TextView = dialog.findViewById(R.id.nominalTextView)
        val priceTextView: TextView = dialog.findViewById(R.id.priceTextView)
        val exitDialogButton: ImageButton = dialog.findViewById(R.id.exitDialogButton)
        val resultTextView: TextView = dialog.findViewById(R.id.resultTextView)
        val converterEditText: EditText = dialog.findViewById(R.id.toConvertEditText)
        val convertButton: Button = dialog.findViewById(R.id.convertButton)
        val tipTextView: TextView = dialog.findViewById(R.id.tipTextView)
        val dialogDateTextView: TextView = dialog.findViewById(R.id.dateTextView)
        val convertLabel: TextView = dialog.findViewById(R.id.toConvertLabel)

        tipTextView.visibility = View.GONE
        val onClickListener = View.OnClickListener(){
            if(converterEditText.text.isEmpty()){
                tipTextView.visibility = View.VISIBLE
            }
            else{
                var toConvert: Double = converterEditText.text.toString().toDouble()
                var result: BigDecimal = convertToRub(toConvert, currency)
                if(result != BigDecimal(0.0)){
                    resultTextView.text = result.toString()
                    tipTextView.visibility = View.GONE
                }
                else{
                    tipTextView.visibility = View.VISIBLE
                }
            }
        }

        nameTextView.text = "${currency.name}(${currency.charCode})"
        nominalTextView.text = currency.nominal.toString()
        priceTextView.text = currency.value.toString()
        convertLabel.text = currency.charCode
        dialogDateTextView.text = dateTextView!!.text
        exitDialogButton.setOnClickListener {dialog.dismiss()}
        convertButton.setOnClickListener(onClickListener)

        dialog.show()
    }

    private fun convertToRub(toConvert: Double, currency: CurrencyModel): BigDecimal{
        var result: BigDecimal? = null

        if(toConvert % currency.nominal.toDouble() == 0.0){
            result = (BigDecimal(currency.value * toConvert) / BigDecimal(currency.nominal))
        }

        return result?: BigDecimal(0.0)

    }

}