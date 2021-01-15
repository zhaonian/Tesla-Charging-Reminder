package io.zluan.teslachargingreminder.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION
import android.media.AudioAttributes.USAGE_NOTIFICATION_RINGTONE
import android.media.RingtoneManager.TYPE_NOTIFICATION
import android.media.RingtoneManager.getDefaultUri
import io.zluan.teslachargingreminder.MainActivity
import io.zluan.teslachargingreminder.R

class NotificationManager(private val context: Context) {

    fun sendNotification(body: String) {
        val notificationChannelId = "Tesla Charging Reminder Channel Id"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val ringtoneManager = getDefaultUri(TYPE_NOTIFICATION)
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(USAGE_NOTIFICATION_RINGTONE)
            .setContentType(CONTENT_TYPE_SONIFICATION).build()
        val notificationChannel = NotificationChannel(
            notificationChannelId,
            /* name= */ "Tesla Charging Reminder",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Tesla Charging Reminder Channel"
            enableLights(/* lights= */ true)
            lightColor = Color.RED
            enableVibration(true)
            vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            setSound(ringtoneManager, audioAttributes)
        }
        notificationManager.createNotificationChannel(notificationChannel)

        val pendingIntent = Intent(context, MainActivity::class.java).let { notificationIntent ->
            PendingIntent.getActivity(context,  /* requestCode= */ 0, notificationIntent, /* flags= */ 0)
        }

        val notification = Notification.Builder(context, notificationChannelId)
            .setContentTitle("Tesla Charging Reminder")
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_baseline_battery_charging_full_24)
            .setShowWhen(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(1, notification)
    }
}
