package com.example.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("BootReceiver", "Reboot detected, rescheduling alarms...")
            val pendingResult = goAsync()
            CoroutineScope(Dispatchers.Default).launch {
                try {
                    NotificationScheduler.scheduleAlarms(context)
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}
