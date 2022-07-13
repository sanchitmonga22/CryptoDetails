package com.example.cryptodetails.ui.home

import android.widget.AdapterView
import androidx.lifecycle.ViewModel
import com.example.cryptodetails.data.Repository
import com.example.cryptodetails.model.Currency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {

    var searchText: MutableStateFlow<String> = MutableStateFlow<String>("")

    var codeName: MutableStateFlow<String> = MutableStateFlow<String>("")

    var fullName: MutableStateFlow<String> = MutableStateFlow<String>("")

    private var _currencyStateFlow = MutableStateFlow<ArrayList<Currency>?>(ArrayList())
    val currencyStateFlow = _currencyStateFlow.asStateFlow()

    val searchItemClickListener =
        AdapterView.OnItemClickListener { parent, _, position, _ ->
            this@HomeViewModel.codeName.value =
                (parent?.adapter?.getItem(position) as Currency).name
            this@HomeViewModel.fullName.value =
                (parent.adapter?.getItem(position) as Currency).fullName
        }

    fun makeAPICall() {
        Repository.getCurrencies(_currencyStateFlow)
    }
}