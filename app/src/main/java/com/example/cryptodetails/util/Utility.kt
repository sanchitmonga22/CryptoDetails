package com.example.cryptodetails.util

import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.net.InetAddress
import java.net.UnknownHostException


object Utility {

    fun isConnectedToInternet(): Boolean = isNetworkConnected() && isInternetAvailable()

    private fun isNetworkConnected(): Boolean {
        var result = false
        val connectivityManager =
            ContextHolder.context?.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val actNw =
                connectivityManager.getNetworkCapabilities(
                    connectivityManager.activeNetwork ?: return false
                ) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
        }
        return result
    }


    private fun isInternetAvailable(): Boolean {
        try {
            val address: InetAddress = InetAddress.getByName("www.google.com")
            return !address.equals("")
        } catch (e: UnknownHostException) {
            // Log error
        }
        return false
    }

    fun writeBytesAsImage(bytes: ByteArray): File {
        val path = ContextHolder.context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile("my_file", ".jpg", path)
        val os = FileOutputStream(file);
        os.write(bytes);
        os.close();
        return file
    }
}