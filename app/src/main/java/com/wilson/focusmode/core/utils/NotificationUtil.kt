package com.wilson.focusmode.core.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import com.wilson.focusmode.R

class NotificationUtil(private val context: Context) {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private var lastNotificationTime = 0L
    private val throttle = 5000L

    fun createFocusChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.notif_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = context.getString(R.string.notif_channel_description)
            enableLights(true)
            enableVibration(true)
        }
        notificationManager.createNotificationChannel(channel)
    }

    fun postDistractionNotification(title: String, body: String, icon: Int) {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastNotificationTime < throttle) return

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
        lastNotificationTime = currentTime
    }

    fun buildForegroundNotification(
        title: String,
        body: String,
        icon: Int,
        pendingIntent: PendingIntent? = null,
    ): Notification {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(icon)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setOngoing(false)
            .build()
    }

    companion object {
        const val CHANNEL_ID = "focus_distraction_channel"
        const val NOTIFICATION_ID = 333
        const val ONGOING_NOTIFICATION_ID = 666
    }
}