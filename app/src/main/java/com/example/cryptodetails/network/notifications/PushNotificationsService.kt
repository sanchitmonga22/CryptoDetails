package com.example.cryptodetails.network.notifications

import android.annotation.SuppressLint
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class PushNotificationsService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        // message.data can use custom fields to show in the notification and send using firebase.
        val title = message.notification?.title
        val text = message.notification?.body
        val data = message.data
        super.onMessageReceived(message)
    }
}