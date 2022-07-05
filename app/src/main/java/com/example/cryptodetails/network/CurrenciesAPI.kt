package com.example.cryptodetails.network

import com.example.cryptodetails.model.DataFromAPI
import retrofit2.Response
import retrofit2.http.GET

interface CurrenciesAPI {
    // https://api.kucoin.com/api/v1/currencies
    @GET("api/v1/currencies")
    suspend fun getCurrencies(): Response<DataFromAPI>
}