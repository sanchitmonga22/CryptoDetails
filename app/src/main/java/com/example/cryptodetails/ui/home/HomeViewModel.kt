package com.example.cryptodetails.ui.home

import android.widget.AdapterView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cryptodetails.data.Repository
import com.example.cryptodetails.model.Currency

class HomeViewModel : ViewModel() {

    var searchText: MutableLiveData<String> = MutableLiveData<String>().apply { value = "" }

    var codeName: MutableLiveData<String> = MutableLiveData<String>().apply { value = "" }

    var fullName: MutableLiveData<String> = MutableLiveData<String>().apply { value = "" }

    private var currenciesLiveData = MutableLiveData<ArrayList<Currency>>()

    fun getCurrenciesData(): MutableLiveData<ArrayList<Currency>> {
        return currenciesLiveData
    }

    val searchItemClickListener =
        AdapterView.OnItemClickListener { parent, _, position, _ ->
            this@HomeViewModel.codeName.value =
                (parent?.adapter?.getItem(position) as Currency).name
            this@HomeViewModel.fullName.value =
                (parent.adapter?.getItem(position) as Currency).fullName
        }

    init {
        Repository.getCurrencies(getCurrenciesData())
    }
}