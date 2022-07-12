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
        if (intent?.action?.equals(ACTION) == true) {
            Toast.makeText(context, intent.getStringExtra(EXTRA_DATA), Toast.LENGTH_LONG).show()
        }
    }

}