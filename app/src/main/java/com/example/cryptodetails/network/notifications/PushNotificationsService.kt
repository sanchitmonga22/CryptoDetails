package com.example.cryptodetails.network.notifications

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushNotificationsService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)

    }

    override fun onMessageReceived(message: RemoteMessage) {
        // message.data can use custom fields to show in the notification and send using firebase.
        val title = message.notification?.title
        val text = message.notification?.body
        val data = message.data
        super.onMessageReceived(message)
    }
}