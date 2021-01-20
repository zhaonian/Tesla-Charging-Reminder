package io.zluan.teslachargingreminder.model

/** Authentication result : success (user details) or error message. */
data class LoginResult(
    val token: String? = null,
    val error: Int? = null
)

/** Data validation state of the login form. */
data class LoginFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)
