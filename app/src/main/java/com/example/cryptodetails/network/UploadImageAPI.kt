package com.example.cryptodetails.network

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UploadImageAPI {

    @Multipart
    @POST("upload")
    fun uploadImage(@Part part: MultipartBody.Part): Call<ResponseBody>
}