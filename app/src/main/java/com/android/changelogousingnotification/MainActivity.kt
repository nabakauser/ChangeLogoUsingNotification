package com.android.changelogousingnotification

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.changelogousingnotification.databinding.ActivityMainBinding
import com.android.changelogousingnotification.test2.NotificationManagerHelper

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var notificationHelper: NotificationManagerHelper

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) { Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissionLauncher
        notificationHelper = NotificationManagerHelper(applicationContext)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpUi()
    }

    private fun setUpUi() {
        checkPermissions()
        setUpListeners()
    }

    private fun setUpListeners() {
        binding.uiBtnNotification.setOnClickListener {
            notificationHelper.showTestNotification(binding.uiEtLogo.text.toString())
        }
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            else requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

fun Context.changeAppIcon(
    enabledAlias: String = "",
    allAliases: List<String> = listOf()
) {
    Log.d("AppLog", "$enabledAlias -- $allAliases")
    allAliases.forEach { alias ->
        packageManager.setComponentEnabledSetting(
            ComponentName(this, alias),
            if (alias == enabledAlias)
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED
            else
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }
}