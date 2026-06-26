package com.example.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.DadajonApp
import java.util.Calendar

object NotificationScheduler {
    const val TYPE_BIRTHDAY = 1
    const val TYPE_ANNIVERSARY = 2

    suspend fun scheduleAlarms(context: Context) {
        val app = context.applicationContext as? DadajonApp ?: return
        val settings = app.repository.getSettingsDirect()

        if (!settings.isNotificationEnabled) {
            cancelAlarms(context)
            return
        }

        // Schedule Birthday Alarm
        scheduleAlarmForDate(
            context = context,
            type = TYPE_BIRTHDAY,
            month = settings.birthdayMonth,
            day = settings.birthdayDay
        )

        // Schedule Anniversary Alarm
        scheduleAlarmForDate(
            context = context,
            type = TYPE_ANNIVERSARY,
            month = settings.anniversaryMonth,
            day = settings.anniversaryDay
        )
    }

    private fun scheduleAlarmForDate(context: Context, type: Int, month: Int, day: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("notification_type", type)
        }

        // Use unique request code per type
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            type,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerTime = getNextTriggerTime(month, day)

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        pendingIntent
                    )
                } else {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        pendingIntent
                    )
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            }
            Log.d("NotificationScheduler", "Scheduled alarm type $type for month $month, day $day at $triggerTime")
        } catch (e: Exception) {
            Log.e("NotificationScheduler", "Failed to schedule alarm", e)
        }
    }

    fun cancelAlarms(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        
        listOf(TYPE_BIRTHDAY, TYPE_ANNIVERSARY).forEach { type ->
            val intent = Intent(context, NotificationReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                type,
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel()
            }
        }
        Log.d("NotificationScheduler", "Cancelled all alarms")
    }

    private fun getNextTriggerTime(month: Int, day: Int): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.MONTH, month - 1)
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR_OF_DAY, 9) // 09:00 AM
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.YEAR, 1)
        }
        return calendar.timeInMillis
    }
}
