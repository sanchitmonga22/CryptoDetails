package com.example.cryptodetails.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import com.example.cryptodetails.databinding.ListItemsSearchBarBinding
import com.example.cryptodetails.model.Currency
import java.util.*

class CurrencyListAdapter(context: Context, objects: List<Currency>) :
    ArrayAdapter<Currency>(context, 0, objects) {

    val currencyObjects = ArrayList(objects)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var rootView = convertView
        val binding: ListItemsSearchBarBinding?
        if (rootView == null) {
            binding = ListItemsSearchBarBinding.inflate(LayoutInflater.from(context))
            rootView = binding.root
        } else {
            binding = ListItemsSearchBarBinding.bind(rootView)
        }
        val currentSelectedCurrency = getItem(position)
        binding.text1.text = currentSelectedCurrency?.name
        binding.text2.text = currentSelectedCurrency?.fullName
        return rootView
    }

    override fun getFilter(): Filter {
        return currencyFilter
    }

    private val currencyFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            val suggestions = ArrayList<Currency>()
            if (constraint == null || constraint.isEmpty()) {
                suggestions.addAll(currencyObjects)
            } else {
                val searchQuery = constraint.toString().lowercase(Locale.ROOT).trim()
                currencyObjects.forEach {
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