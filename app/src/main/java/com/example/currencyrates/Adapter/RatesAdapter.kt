package com.example.currencyrates.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.currencyrates.Model.Base
import com.example.currencyrates.R
import com.mynameismidori.currencypicker.ExtendedCurrency
import java.util.*

class RatesAdapter (private val context: Context, private var base: Base) :
    RecyclerView.Adapter<RatesAdapter.RatesViewHolder>() {

    private lateinit var names: List<String>
    private lateinit var values: List<Double>

    init {
        names = base.rates.getDataAsMap().keys.toList()
        values = base.rates.getDataAsMap().values.toList()
    }

    class RatesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivFlag: ImageView
        val tvNameShort: TextView
        val tvNameLong: TextView
        val tvRateValue: TextView

        init {
            ivFlag = itemView.findViewById(R.id.ivFlag)
            tvNameShort = itemView.findViewById(R.id.tvCurrencyShort)
            tvNameLong = itemView.findViewById(R.id.tvCurrencyLong)
            tvRateValue = itemView.findViewById(R.id.tvCurrentValue)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesViewHolder {
        val view: View = LayoutInflater.from(context)
            .inflate(R.layout.item_single_currency, parent, false)
        return RatesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return names.size
    }

    override fun onBindViewHolder(holder: RatesViewHolder, position: Int) {


        val currency = Currency.getInstance(names[position])
        val currencyFlag: ExtendedCurrency =
            ExtendedCurrency.getCurrencyByISO(currency.currencyCode)
        holder.ivFlag.setImageDrawable(context.getDrawable(currencyFlag.flag))
        holder.tvNameShort.text = names[position]
        holder.tvNameLong.text = currency.displayName
        holder.tvRateValue.text = values[position].toString()

    }

    fun setData(rates: Base) {
        names = rates.rates.getDataAsMap().keys.toList()
        values = rates.rates.getDataAsMap().values.toList()
        notifyDataSetChanged()
    }

}