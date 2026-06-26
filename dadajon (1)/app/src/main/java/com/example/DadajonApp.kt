package com.example

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.data.AppDatabase
import com.example.data.SettingsRepository
import com.example.music.AmbientMusicPlayer
import com.example.notifications.NotificationScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DadajonApp : Application() {

    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { SettingsRepository(database.settingsDao()) }
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        AmbientMusicPlayer.initialize(this)
        
        // Schedule alarms on background thread to prevent cold start blockages
        applicationScope.launch {
            NotificationScheduler.scheduleAlarms(this@DadajonApp)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Oilaviy Tabriklar"
            val descriptionText = "Tug'ilgan kun va nikoh yilligi tabriklari bildirishnomalari"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "dadajon_celebrations_channel"
    }
}
