package com.android.changelogousingnotification

import android.util.Log
import com.android.changelogousingnotification.test2.NotificationManagerHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "AppLog"
    }

    private lateinit var notificationHelper: NotificationManagerHelper

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")
        notificationHelper = NotificationManagerHelper(applicationContext)

        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            notificationHelper.showTestNotification(it.body ?: "")
        }
    }
}