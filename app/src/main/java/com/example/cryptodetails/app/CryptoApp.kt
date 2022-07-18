package com.example.cryptodetails.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class CryptoApp : Application() {

    companion object {
        const val CHANNEL_1 = "Channel1"
        const val CHANNEL_2 = "Channel2"
    }

    override fun onCreate() {
        super.onCreate()
        createPNChannels()
    }

    private fun createPNChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val highImportanceChannel1 =
                NotificationChannel(
                    CHANNEL_1,
                    "HIGH IMPORTANCE CHANNEL 1",
                    NotificationManager.IMPORTANCE_HIGH
                )
            highImportanceChannel1.description = "This notification is of HIGH importance"


            val lowImportanceNotificationChannel2 =
                NotificationChannel(
                    CHANNEL_2,
                    "LOW IMPORTANCE CHANNEL 1",
                    NotificationManager.IMPORTANCE_LOW
                )
            lowImportanceNotificationChannel2.description = "This notification is of low importance"
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(highImportanceChannel1)
            manager.createNotificationChannel(lowImportanceNotificationChannel2)
        }
    }
}