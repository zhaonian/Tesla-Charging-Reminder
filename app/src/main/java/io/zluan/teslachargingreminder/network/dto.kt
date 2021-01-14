package io.zluan.teslachargingreminder.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Stats(
    @Json(name = "title")
    val nickname: String,
    @Json(name = "id")
    val battery: Int
)
