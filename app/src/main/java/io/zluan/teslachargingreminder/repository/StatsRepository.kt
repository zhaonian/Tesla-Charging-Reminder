package io.zluan.teslachargingreminder.repository

import android.content.Context
import android.util.Log
import io.zluan.teslachargingreminder.network.TeslaNetwork
import io.zluan.teslachargingreminder.notification.NotificationManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StatsRepository(private val context: Context) {

    private val notificationManager = NotificationManager(context)

    suspend fun refreshStats() {
        withContext(Dispatchers.IO) {
            val fetchedStats = TeslaNetwork.teslaService.getCarStats()
            Log.d("StatsRepository", "nickname: ${fetchedStats.nickname} and battery: ${fetchedStats.battery}");
            // Used to update the app's UI and send push notifications.
            notificationManager.sendNotification(fetchedStats.nickname, fetchedStats.battery)
        }
    }
}
