package com.android.changelogousingnotification.test2

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.changelogousingnotification.R
import com.android.changelogousingnotification.changeAppIcon

class NotificationManagerHelper(private val context: Context) {

    companion object {
        private const val CHANNEL_ID = "app_notification_channel"
        private const val NOTIFICATION_ID = 100
        const val ACTION_DISMISS = "com.example.app.ACTION_DISMISS"
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val name = "App Notifications"
        val descriptionText = "General notifications for the app"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun showTestNotification(logo: String) {
        val toastIntent = Intent(context, ToastReceiver::class.java).apply {
            putExtra("logoData",logo)
        }
        val toastPendingIntent: PendingIntent = PendingIntent.getBroadcast(
            context, 1, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        handleNotificationData(
            context, logo
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("APP TITLE - $logo")
            .setContentText("Hello, this is test notification for $logo")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(NotificationCompat.BigTextStyle().bigText("Hello, this is a test notification"))
            .setAutoCancel(true)
            .addAction(0, "Toast", toastPendingIntent)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                return
            }
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun handleNotificationData(
        context: Context?,
        data: String
    ) {
        val alias = when (data) {
            "colored" ->{
                Log.d("AppLog", "colored: $data")
                "${context?.packageName}.LogoOneActivity"
            }
            "blue" -> {
                Log.d("AppLog", "blue: $data")
                "${context?.packageName}.LogoTwoActivity"
            }
            else -> {
                Log.d("AppLog", "else: $data")
                "${context?.packageName}.MainActivity"
            }
        }

        context?.changeAppIcon(
            alias,
            listOf(
                "${context.packageName}.MainActivity",
                "${context.packageName}.LogoOneActivity",
                "${context.packageName}.LogoTwoActivity",
            )
        )
    }
}