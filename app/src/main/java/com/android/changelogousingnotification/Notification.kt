package com.android.changelogousingnotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.Random

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "AppLog"
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")

        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            sendNotification(it.body)
        }
    }

    private fun handleNotificationData(data: String) {
        Log.d(TAG, "Message Notification Body: $data")
        val alias = when (data) {
            "colored" ->{
                Log.d(TAG, "colored: $data")
                "$packageName.LogoOneActivity"
            }
            "blue" -> {
                Log.d(TAG, "blue: $data")
                "$packageName.LogoTwoActivity"
            }
            else -> {
                Log.d(TAG, "else: $data")
                "$packageName.MainActivity"
            }
        }

        Intent("CHANGE_APP_ICON").apply {
            putExtra("iconAlias", alias)
            LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(this)
        }
    }

    private fun sendNotification(messageBody: String?) {

        val message = messageBody ?: "You have a new notification"
        val receiverIntent = Intent("com.example.NOTIFICATION_RECEIVED")
        receiverIntent.putExtra("message", message)
        sendBroadcast(receiverIntent)

        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val channelId = "Notification_Channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Title for the notification")
            .setContentText(messageBody)
            .setAutoCancel(false)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "Default Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        val notificationId = Random().nextInt()
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}