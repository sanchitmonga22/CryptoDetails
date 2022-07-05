package com.example.cryptodetails.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cryptodetails.data.Repository
import com.example.cryptodetails.model.Currency

class HomeViewModel : ViewModel() {

    private val _searchText = MutableLiveData<String>().apply {
        value = ""
    }

    val text: LiveData<String> = _searchText

    private var currenciesLiveData = MutableLiveData<ArrayList<Currency>>()

    // To get the list of quotes.
    fun getCurrenciesRepository(): LiveData<ArrayList<Currency>> {
        return currenciesLiveData
    }

    init {
        currenciesLiveData = Repository.getCurrencies()
    }
}