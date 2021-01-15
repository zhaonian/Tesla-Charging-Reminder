package io.zluan.teslachargingreminder.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AccessTokenRequest(
    @Json(name = "email")
    val email: String,
    @Json(name = "password")
    val password: String,
    @Json(name = "client_secret")
    val clientSecret: String,
    @Json(name = "client_id")
    val clientId: String,
    @Json(name = "grant_type")
    val grantType: String
)

@JsonClass(generateAdapter = true)
data class AccessTokenResponse(
    @Json(name = "access_token")
    val accessToken: String // expires every 45 days
)

@JsonClass(generateAdapter = true)
data class Vehicle(
    @Json(name = "id")
    val id: Long,
    @Json(name = "display_name")
    val displayName: String
)

@JsonClass(generateAdapter = true)
data class VehicleList(
    @Json(name = "response")
    val vehicles: List<Vehicle>,
    @Json(name = "count")
    val count: Int
)

@JsonClass(generateAdapter = true)
data class ChargeStateResponse(
    @Json(name = "response")
    val chargeState: ChargeState
)

@JsonClass(generateAdapter = true)
data class ChargeState(
    @Json(name = "battery_level")
    val batteryLevel: Int,
    @Json(name = "battery_range")
    val batteryRange: Float,
    @Json(name = "est_battery_range")
    val estBatteryRange: Float
)
