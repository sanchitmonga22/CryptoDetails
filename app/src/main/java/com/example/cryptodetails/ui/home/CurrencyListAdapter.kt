package com.example.cryptodetails.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.example.cryptodetails.R
import com.example.cryptodetails.model.Currency
import java.util.*

class CurrencyListAdapter(
    context: Context,
    resource: Int,
    textViewResourceId: Int,
    objects: List<Currency>
) : ArrayAdapter<Currency>(context, resource, textViewResourceId, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var rootView = convertView
        if (rootView == null) {
//            convertView =
//                ListItemsSearchBarBinding.inflate(LayoutInflater.from(context), parent, false).root
            rootView =
                LayoutInflater.from(context).inflate(R.layout.list_items_search_bar, parent, false)
        }
        val currentSelectedCurrency = getItem(position)
        rootView?.findViewById<TextView>(R.id.text1)?.text = currentSelectedCurrency?.name
        rootView?.findViewById<TextView>(R.id.text2)?.text = currentSelectedCurrency?.fullName
        return rootView!!
    }

    override fun getFilter(): Filter {
        return currencyFilter
    }

    private val currencyFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            val suggestions = ArrayList<Currency>()
            if (constraint == null || constraint.isEmpty()) {
                suggestions.addAll(objects)
            } else {
                val searchQuery = constraint.toString().lowercase(Locale.ROOT).trim()
                objects.forEach {
                    if (it.name.lowercase(Locale.ROOT).trim()
                            .contains(searchQuery) || it.fullName.lowercase(Locale.ROOT).trim()
                            .contains(searchQuery)
                    ) {
                        suggestions.add(it)
                    }
                }
            }
            results.values = suggestions
            results.count = suggestions.size
            return results
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            clear()
            addAll(results?.values as ArrayList<Currency>)
            notifyDataSetChanged()
        }

        override fun convertResultToString(resultValue: Any?): CharSequence {
            return (resultValue as Currency).name
        }
    }
}