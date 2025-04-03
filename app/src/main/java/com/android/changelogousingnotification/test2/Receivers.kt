package com.android.changelogousingnotification.test2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.android.changelogousingnotification.changeAppIcon

class NotificationDismissReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == NotificationManagerHelper.ACTION_DISMISS) {
            val notificationId = intent.getIntExtra("notificationId", -1)
            if (notificationId != -1 && context != null) {
                NotificationManagerCompat.from(context).cancel(notificationId)
            }
        }
    }
}

class ToastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val logo = intent?.getStringExtra("logoData")
       handleNotificationData(context,logo ?: "")
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