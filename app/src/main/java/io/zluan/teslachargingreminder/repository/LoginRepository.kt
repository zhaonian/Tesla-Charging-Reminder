package io.zluan.teslachargingreminder.repository

import android.content.Context
import io.zluan.teslachargingreminder.network.*

class LoginRepository(private val context: Context) {

    private val responseHandler = ResponseHandler()

    suspend fun getAuthToken(username: String, password: String): Resource<AccessTokenResponse> {
        return try {
            responseHandler.handleSuccess(
                TeslaService.endpoints.getAccessToken(
                    AccessTokenRequest(
                        email = username,
                        password = password,
                        clientSecret = "c7257eb71a564034f9419ee651c7d0e5f7aa6bfbd18bafb5c5c033b093bb2fa3",
                        clientId = "81527cff06843c8634fdc09e8ac0abefb46ac849f38fe1e431c2ef2106796384",
                        grantType = "password"
                    )
                )
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }
}
