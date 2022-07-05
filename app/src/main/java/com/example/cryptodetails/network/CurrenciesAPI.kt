package com.example.cryptodetails.network

import com.example.cryptodetails.model.DataFromAPI
import retrofit2.Response
import retrofit2.http.GET

interface CurrenciesAPI {
    @GET("/currencies")
    suspend fun getCurrencies(): Response<DataFromAPI>
}