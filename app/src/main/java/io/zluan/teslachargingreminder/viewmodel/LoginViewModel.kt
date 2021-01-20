package io.zluan.teslachargingreminder.viewmodel

import android.app.Service
import android.content.Context
import android.content.SharedPreferences
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.zluan.teslachargingreminder.R
import io.zluan.teslachargingreminder.extension.PREF_FILE_KEY
import io.zluan.teslachargingreminder.extension.setAccessToken
import io.zluan.teslachargingreminder.extension.setPassword
import io.zluan.teslachargingreminder.extension.setUsername
import io.zluan.teslachargingreminder.model.LoginFormState
import io.zluan.teslachargingreminder.model.LoginResult
import io.zluan.teslachargingreminder.network.Status
import io.zluan.teslachargingreminder.repository.LoginRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(
    private val context: Context,
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val sharedPrefs: SharedPreferences by lazy {
        context.getSharedPreferences(PREF_FILE_KEY, Service.MODE_PRIVATE)
    }

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    fun login(username: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val tokenResource = loginRepository.getAuthToken(username, password)
            when (tokenResource.status) {
                Status.SUCCESS -> {
                    tokenResource.data?.let { tokenData ->
                        _loginResult.postValue(LoginResult(tokenData.accessToken))
                        sharedPrefs.apply {
                            setUsername(username)
                            setPassword(password)
                            setAccessToken(tokenData.accessToken)
                        }
                    }
                }
                Status.ERROR -> {
                    _loginResult.postValue(LoginResult(error = R.string.login_failed))
                }
            }
        }
    }

    private fun isUserNameValid(username: String): Boolean {
        return username.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(username).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}
