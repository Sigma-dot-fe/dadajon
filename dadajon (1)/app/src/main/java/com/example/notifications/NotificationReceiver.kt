package com.example.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.DadajonApp
import com.example.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getIntExtra("notification_type", NotificationScheduler.TYPE_BIRTHDAY)
        
        val title: String
        val text: String
        
        if (type == NotificationScheduler.TYPE_BIRTHDAY) {
            title = "🎉 Tug'ilgan kun muborak!"
            text = """
                Aziz Dadajon!
                Tug'ilgan kuningiz bilan chin yurakdan tabriklayman.
                Sizga uzoq umr, mustahkam sog'lik, baxt va omad tilayman.
                Har doim baxtli bo'ling.
                Sizni juda yaxshi ko'raman.
                
                Hurmat bilan,
                O'g'lingiz Javohir ❤️
            """.trimIndent()
        } else {
            title = "💍 Nikoh yilligi muborak!"
            text = """
                Aziz Dadajon va Onajon!
                Nikoh yilligingiz muborak bo'lsin.
                Sizlarga sog'lik, baxt, muhabbat va uzoq umr tilayman.
                Har doim baxtli yashang.
                Sizlar bilan faxrlanaman.
                
                O'g'lingiz Javohir ❤️
            """.trimIndent()
        }

        // Intent to open MainActivity and show the greeting screen directly
        val mainIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("show_greeting_type", type)
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            type + 100, // unique request code
            mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, DadajonApp.CHANNEL_ID)
            .setSmallIcon(android.R.drawable.btn_star_big_on) // Beautiful star icon for notifications
            .setContentTitle(title)
            .setContentText(title) // fallback
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_EVENT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(type, notification)

        // Reschedule alarms for the next year in a coroutine
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
