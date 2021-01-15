package io.zluan.teslachargingreminder.network

import android.app.Service
import io.zluan.teslachargingreminder.TeslaChargingReminderApplication
import io.zluan.teslachargingreminder.extension.PREF_FILE_KEY
import io.zluan.teslachargingreminder.extension.getAccessToken
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/** All Tesla's network communications. */
interface TeslaService {
    @POST("/oauth/token")
    suspend fun getAccessToken(@Body request: AccessTokenRequest): AccessTokenResponse

    @GET("/api/1/vehicles")
    suspend fun getVehicleList(): VehicleList

    @GET("/api/1/vehicles/{id}/data_request/charge_state")
    suspend fun getChargeState(@Path("id") id: Long): ChargeStateResponse

    companion object {

        private val sharedPrefs = TeslaChargingReminderApplication.context.getSharedPreferences(
            PREF_FILE_KEY,
            Service.MODE_PRIVATE
        )

        // for Logging
        private val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        private val httpClientBuilder = OkHttpClient.Builder().addInterceptor { chain ->
            val newRequestBuilder = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
            sharedPrefs.getAccessToken()?.let { accessToken ->
                newRequestBuilder.addHeader("Authorization", "Bearer $accessToken")
            }
            chain.proceed(newRequestBuilder.build())
        }.addInterceptor(interceptor)

        val endpoints: TeslaService = Retrofit.Builder()
            .baseUrl("https://owner-api.teslamotors.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(httpClientBuilder.build())
            .build()
            .create(TeslaService::class.java)
    }
}
