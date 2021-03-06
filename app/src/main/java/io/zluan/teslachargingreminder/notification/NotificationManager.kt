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
import android.widget.RemoteViews
import io.zluan.teslachargingreminder.MainActivity
import io.zluan.teslachargingreminder.R

class NotificationManager(private val context: Context) {

    fun sendNotification(title: String, body: String, useCustomView: Boolean) {
        val notificationChannelId = "Tesla Charging Reminder Channel Id"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val ringtoneManager = getDefaultUri(TYPE_NOTIFICATION)
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(USAGE_NOTIFICATION_RINGTONE)
            .setContentType(CONTENT_TYPE_SONIFICATION).build()
        val notificationChannel = NotificationChannel(
            notificationChannelId,
            /* name= */ context.getString(R.string.app_name),
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

        val expandedLayout = RemoteViews(context.packageName, R.layout.notification_expanded).apply {
            setTextViewText(R.id.description, body)
        }
        val summaryLayout = RemoteViews(context.packageName, R.layout.notification_summary).apply {
            setTextViewText(R.id.title, title)
        }

        val notificationBuilder = Notification.Builder(context, notificationChannelId)
            .setStyle(Notification.MediaStyle())
            .setSmallIcon(R.drawable.ic_lightning_bolt)
            .setContentIntent(pendingIntent)

        if (useCustomView) {
            notificationBuilder
                .setCustomHeadsUpContentView(summaryLayout)
                .setCustomBigContentView(expandedLayout)
        } else {
            notificationBuilder
                .setColor(context.getColor(R.color.colorAccent))
                .setContentTitle(title)
                .setContentText(body)
                .setShowWhen(true)
                .setColorized(true)
        }

        notificationManager.notify(1, notificationBuilder.build())
    }
}
