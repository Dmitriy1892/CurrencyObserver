package com.coldfier.currencyobserver.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.coldfier.currencyobserver.R
import com.coldfier.currencyobserver.data.repositories.models.Currency

class ChosenCurrencyAdapter(
    context: Context
) : ArrayAdapter<Currency>(context, R.layout.item_chosen_currency) {

    private val noOpFilter = object : Filter() {
        private val noOpResult = FilterResults()
        override fun performFiltering(constraint: CharSequence?): FilterResults = noOpResult
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {}
    }

    override fun getFilter(): Filter = noOpFilter

    override fun addAll(collection: MutableCollection<out Currency>) {
        clear()
        super.addAll(collection)
    }

    override fun addAll(vararg items: Currency?) {
        clear()
        super.addAll(*items)
    }

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val item = getItem(position)
        val currencyCode = item?.currencyCode ?: ""
        val name = item?.name ?: ""
        (view as TextView).text = "$currencyCode: $name"
        return view
    }
}