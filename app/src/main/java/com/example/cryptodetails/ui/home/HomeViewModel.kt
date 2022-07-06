package com.example.cryptodetails.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cryptodetails.data.Repository
import com.example.cryptodetails.model.Currency

class HomeViewModel : ViewModel() {

    var searchText: MutableLiveData<String> = MutableLiveData<String>().apply { value = "" }

    var codeName: MutableLiveData<String> = MutableLiveData<String>().apply { value = "" }

    var fullName: MutableLiveData<String> = MutableLiveData<String>().apply { value = "" }

    private var currenciesLiveData = MutableLiveData<ArrayList<Currency>>()

    // To get the list of quotes.
    fun getCurrenciesRepository(): LiveData<ArrayList<Currency>> {
        return currenciesLiveData
    }

//    fun getCodeValueFromSearchQuery(searchWord: String): String {
//        var returnValue = ""
//        getSelectedSearchQueryCurrencyHelper(searchWord)?.let {
//            returnValue = it.name
//        }
//        return returnValue
//    }

//    private fun getSelectedSearchQueryCurrencyHelper(searchWord: String): Currency? {
//        var searchedCurrency: Currency? = null
//        currenciesLiveData.value?.forEach {
//            if (searchWord == it.fullName || searchWord == it.name) {
//                searchedCurrency = it
//                return@forEach
//            }
//        }
//        return searchedCurrency
//    }

//    fun getNameValueFromSearchQuery(searchWord: String): String {
//        var returnValue = ""
//        getSelectedSearchQueryCurrencyHelper(searchWord)?.let {
//            returnValue = it.fullName
//        }
//        return returnValue
//    }
//
//    fun filterQueriesGeneratorHelper(currencyData: ArrayList<Currency>): ArrayList<String> {
//        val currenciesSearchQueries = ArrayList<String>()
//        currencyData.forEach {
//            currenciesSearchQueries.add(it.name)
//            currenciesSearchQueries.add(it.fullName)
//        }
//        return currenciesSearchQueries
//    }

    init {
        currenciesLiveData = Repository.getCurrencies()
    }
}