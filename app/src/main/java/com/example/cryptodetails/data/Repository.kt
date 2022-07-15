package com.example.cryptodetails.data

import android.net.Uri
import android.os.Environment
import android.util.Log
import com.example.cryptodetails.model.Currency
import com.example.cryptodetails.network.CurrenciesAPI
import com.example.cryptodetails.network.LocalHostRetrofit
import com.example.cryptodetails.network.RetrofitHelper
import com.example.cryptodetails.network.UploadImageAPI
import com.example.cryptodetails.util.ContextHolder
import com.example.cryptodetails.util.Utility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

object Repository {

    fun getCurrencies(_currencyStateFlow: MutableStateFlow<ArrayList<Currency>?>) {
        // launching a background task
        CoroutineScope(Dispatchers.Default).launch(Dispatchers.IO) {
            if (!Utility.isConnectedToInternet()) {
                _currencyStateFlow.value = null
            } else {
                // making the network call using retrofit
                val response =
                    RetrofitHelper.getInstance().create(CurrenciesAPI::class.java).getCurrencies()
                response.let {
                    if (response.isSuccessful) {
                        _currencyStateFlow.value = response.body()?.data
                    }
                }
            }
        }
    }

    fun writeBytesAsPdf(bytes: ByteArray): File {
        val path = ContextHolder.context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile("my_file", ".jpg", path)
        val os = FileOutputStream(file);
        os.write(bytes);
        os.close();
        return file
    }

    fun saveImage(imageUri: Uri, byteArray: ByteArray) {
        CoroutineScope(Dispatchers.Default).launch(Dispatchers.IO) {
            try {

                //            val path = Utility.getPath(imageUri)
//                val path = imageUri.path
//            val imageFile = File(path!!)
//                val imageFile = File(Utility.getPath(ContextHolder.context!!, imageUri)!!)
                val imageFile = writeBytesAsPdf(byteArray)
                val requestBody = RequestBody.create(
                    MediaType.parse(
                        ContextHolder.context!!.contentResolver.getType(imageUri)!! // "image/*". "multipart/form-data"
                    ), imageFile
                )

                val parts =
                    MultipartBody.Part.createFormData("profileImage", imageFile.name, requestBody)

                val retroInstance =
                    LocalHostRetrofit.getInstance().create(UploadImageAPI::class.java)
                val call = retroInstance.uploadImage(parts)

                call.enqueue(object : Callback<ResponseBody?> {
                    override fun onResponse(
                        call: Call<ResponseBody?>, response: Response<ResponseBody?>
                    ) {
                        Log.d("TAGGGGGG", "onResponse: ")
                    }

                    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                        Log.d("TAGGGGGG", "onFailure: ")
                    }
                })
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}