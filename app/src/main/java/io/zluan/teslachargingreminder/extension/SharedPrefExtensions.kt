package io.zluan.teslachargingreminder.extension

import android.content.SharedPreferences

const val PREF_FILE_KEY = "io.zluan.teslachargingreminder"

const val ACCESS_TOKEN = "ACCESS_TOKEN"
const val VEHICLE_ID = "VEHICLE_ID"
const val VEHICLE_NAME = "VEHICLE_NAME"

fun SharedPreferences.setAccessToken(value: String) {
    return this.setStringPref(ACCESS_TOKEN, value)
}

fun SharedPreferences.getAccessToken(): String? {
    return this.getString(ACCESS_TOKEN, null)
}

fun SharedPreferences.setVehicleId(value: Long) {
    with (this.edit()) {
        putLong(VEHICLE_ID, value)
        apply()
    }
}

fun SharedPreferences.getVehicleId(): Long {
    return this.getLong(VEHICLE_ID, -1L)
}

fun SharedPreferences.setVehicleName(value: String) {
    with (this.edit()) {
        putString(VEHICLE_NAME, value)
        apply()
    }
}

fun SharedPreferences.getVehicleName(): String {
    return this.getString(VEHICLE_NAME, "") ?: ""
}

private fun SharedPreferences.setStringPref(key: String, value: String) {
    with (this.edit()) {
        putString(key, value)
        apply()
    }
}
