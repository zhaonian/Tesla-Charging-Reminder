package io.zluan.teslachargingreminder.repository

import android.app.Service
import android.content.Context
import android.content.SharedPreferences
import io.zluan.teslachargingreminder.R
import io.zluan.teslachargingreminder.extension.*
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
            val firstVehicleId = sharedPrefs.getVehicleId()
            if (firstVehicleId == -1L) {
                getFirstVehicle()
            }
            val chargeState = TeslaService.endpoints.getChargeState(sharedPrefs.getVehicleId()).chargeState
            if (chargeState.batteryLevel < 25 || chargeState.estBatteryRange < 100f) {
                notificationManager.sendNotification(
                    title = context.getString(R.string.summary_reminder_title, sharedPrefs.getVehicleName()),
                    body = context.getString(
                        R.string.expanded_reminder_description,
                        chargeState.batteryLevel,
                            chargeState.batteryRange,
                        chargeState.estBatteryRange
                    ),
                    useCustomView = sharedPrefs.getUseCustomViewSetting()
                )
            }
        }
    }

    // TODO: expand the app to support multiple vehicles.
    suspend fun getFirstVehicle() {
        withContext(Dispatchers.IO) {
            val vehicleList = TeslaService.endpoints.getVehicleList()
            val firstVehicle = vehicleList.vehicles[0]
            sharedPrefs.setVehicleId(firstVehicle.id)
            sharedPrefs.setVehicleName(firstVehicle.displayName)
        }
    }
}
