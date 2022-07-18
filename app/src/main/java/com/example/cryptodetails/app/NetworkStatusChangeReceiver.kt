package com.example.cryptodetails.app

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.cryptodetails.util.Utility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NetworkStatusChangeReceiver : BroadcastReceiver() {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        CoroutineScope(Dispatchers.Default).launch(Dispatchers.IO) {
            if (!Utility.isConnectedToInternet()) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Disconnected from internet", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

class CustomBroadcastReceiver : BroadcastReceiver() {
    companion object {
        const val ACTION = "com.example.cryptodetails.EXAMPLE_ACTION"
        const val EXTRA_DATA = "com.example.cryptodetails.EXAMPLE_EXTRA_DATA"
    }

    override fun onReceive(context: Context?, intent: Intent?) {

//        can be specified when sending a broadcast using sendOrderedBroadcast()
//        resultCode
//        resultData
//        getResultExtras(true).getString("")
//        setResult()

//        context.sendOrderedBroadcast()
//        define these broadcasts in the manifest file
//        can also define the priority in which we want to run it.\

//        abortBroadcast
//        can be used to abort the broadcast if there are further broadcasts in progress

        // For heavy operations on background threads in broadcasts.
//        val pendingResult = goAsync()
//        ********ASYNC WORK HERE*******
//        pendingResult.finish()


        if (intent?.action?.equals(ACTION) == true) {
            Toast.makeText(context, intent.getStringExtra(EXTRA_DATA), Toast.LENGTH_LONG).show()
        }
    }
}