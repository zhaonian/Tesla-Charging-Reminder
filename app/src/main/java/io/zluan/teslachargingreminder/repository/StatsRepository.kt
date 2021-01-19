package io.zluan.teslachargingreminder.repository

import android.app.Service
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import io.zluan.teslachargingreminder.R
import io.zluan.teslachargingreminder.extension.*
import io.zluan.teslachargingreminder.network.AccessTokenRequest
import io.zluan.teslachargingreminder.network.TeslaService
import io.zluan.teslachargingreminder.notification.NotificationManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StatsRepository(private val context: Context) {

    private val notificationManager = NotificationManager(context)
    private val sharedPrefs: SharedPreferences by lazy {
        context.getSharedPreferences(PREF_FILE_KEY, Service.MODE_PRIVATE)
    }

    suspend fun refreshChargeState() {
        withContext(Dispatchers.IO) {
            notificationManager.sendNotification(
                title = context.getString(R.string.summary_reminder_title, sharedPrefs.getVehicleName()),
                body = context.getString(R.string.expanded_reminder_description, 44, "233", "250")
            )
//            "firstVehicleName has a battery level of {chargeState.batteryLevel}%\n" +
//                    "\t-Range: {chargeState.batteryRange}\n" +
//                    "\t-Est. Range based on your avg. consumption: {chargeState.estBatteryRange}"
//            val firstVehicleId = sharedPrefs.getVehicleId()
//            val firstVehicleName = sharedPrefs.getVehicleName()
//            if (firstVehicleId != -1L) {
//                val chargeState = TeslaService.endpoints.getChargeState(firstVehicleId).chargeState
//                if (chargeState.batteryLevel < 25) {
//
//                }
//            }
        }
    }

    suspend fun getFirstVehicle() {
        withContext(Dispatchers.IO) {
            val vehicleList = TeslaService.endpoints.getVehicleList()
            val firstVehicle = vehicleList.vehicles[0]
            sharedPrefs.setVehicleId(firstVehicle.id)
            sharedPrefs.setVehicleName(firstVehicle.displayName)
        }
    }

    suspend fun getAuthToken() {
        withContext(Dispatchers.IO) {
            val accessTokenResponse = TeslaService.endpoints.getAccessToken(
                AccessTokenRequest(
                    email = "",
                    password = "",
                    clientSecret = "c7257eb71a564034f9419ee651c7d0e5f7aa6bfbd18bafb5c5c033b093bb2fa3",
                    clientId = "81527cff06843c8634fdc09e8ac0abefb46ac849f38fe1e431c2ef2106796384",
                    grantType = "password"
                )
            )
            sharedPrefs.setAccessToken(accessTokenResponse.accessToken)
            Log.d("StatsRepository", "token: ${accessTokenResponse.accessToken}")
        }
    }
}
