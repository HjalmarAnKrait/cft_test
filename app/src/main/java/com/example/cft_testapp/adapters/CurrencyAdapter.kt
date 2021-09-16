package com.example.cft_testapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cft_testapp.R
import com.example.cft_testapp.model.CurrencyModel

class CurrencyAdapter(private val currencyList: List<CurrencyModel>, var mMyOnItemClickListener: MyOnItemClickListener) :
    RecyclerView.Adapter<CurrencyAdapter.MyViewHolder>(){

    class MyViewHolder(itemView: View, val myOnItemClickListener: MyOnItemClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var nameTextView: TextView? = null
        var nominalTextView: TextView? = null
        var priceTextView: TextView? = null

        init {
            nameTextView = itemView.findViewById(R.id.nameTextView)
            nominalTextView = itemView.findViewById(R.id.nominalTextView)
            priceTextView = itemView.findViewById(R.id.priceTextView)
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            myOnItemClickListener.onItemClick(adapterPosition)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)
        return MyViewHolder(itemView, mMyOnItemClickListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currency = currencyList.get(position)
        holder.nameTextView?.text = "${currency.name}(${currency.charCode})"
        holder.nominalTextView?.text = currency.nominal.toString()
        holder.priceTextView?.text = currency.value.toString()
    }

    override fun getItemCount() = currencyList.size

    interface MyOnItemClickListener{
        fun onItemClick(position: Int): Unit
    }

}

