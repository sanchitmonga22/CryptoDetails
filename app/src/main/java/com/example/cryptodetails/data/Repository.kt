package com.example.cryptodetails.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.cryptodetails.model.Currency
import com.example.cryptodetails.network.CurrenciesAPI
import com.example.cryptodetails.network.RetrofitHelper
import com.example.cryptodetails.util.Utility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Repository {

    fun getCurrencies(): MutableLiveData<ArrayList<Currency>> {
        val currenciesMutableLiveData: MutableLiveData<ArrayList<Currency>> =
            MutableLiveData<ArrayList<Currency>>()
        if (!Utility.isConnectedToInternet()) {
            Log.d("INTERNET", "NOT CONNNECTEDDDDD")
            return MutableLiveData<ArrayList<Currency>>()
        }
        // launching a background task
        CoroutineScope(Dispatchers.Default).launch(Dispatchers.IO) {
            // making the network call using retrofit
            val response =
                RetrofitHelper.getInstance().create(CurrenciesAPI::class.java).getCurrencies()
            response.let {
                if (response.isSuccessful) {
                    currenciesMutableLiveData.postValue(response.body()?.data)
                }
            }
        }
        return currenciesMutableLiveData
    }
}