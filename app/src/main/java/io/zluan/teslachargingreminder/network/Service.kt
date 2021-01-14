package io.zluan.teslachargingreminder.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

/** All Tesla's network communications. */
interface TeslaService {
    @GET("1")
    suspend fun getCarStats(): Stats
}

/** Main entry point for network access. */
object TeslaNetwork {
    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/todos/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val teslaService = retrofit.create(TeslaService::class.java)
}
