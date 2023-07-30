package infinuma.android.shows.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import infinuma.android.shows.data.model.LoginRequest
import infinuma.android.shows.data.model.LoginResponse
import infinuma.android.shows.networking.ApiModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private var accessToken: String = ""
    private var client: String = ""
    private var uid: String = ""

    private val _loginLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val loginLiveData: LiveData<Boolean> = _loginLiveData

    fun onLoginButtonClicked(username: String, password: String) {
        viewModelScope.launch {
            try {
                // Call the login API
                val response = login(username, password)

                // Check if the response is successful
                if (response.isSuccessful) {
                    // Update the token data properties
                    accessToken = response.headers()["access-token"] ?: ""
                    client = response.headers()["client"] ?: ""
                    uid = response.headers()["uid"] ?: ""

                    // Notify the login result through MutableLiveData
                    _loginLiveData.value = true
                } else {
                    // Login failed, notify the login result
                    _loginLiveData.value = false
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error: ${e.message}", e)
                _loginLiveData.value = false
            }
        }
    }

    fun getAccessToken(): String = accessToken
    fun getClient(): String = client
    fun getUid(): String = uid

    private suspend fun login(username: String, password: String): Response<LoginResponse> =
        withContext(Dispatchers.IO) {
            ApiModule.retrofit.login(
                request = LoginRequest(
                    email = username,
                    password = password
                )
            )
        }
}

//    fun onLoginButtonClicked(username: String, password: String) = viewModelScope.launch { ----------OLD LOGIN BUTTON CLICKED
//        try {
//            val loginResponse = login(username, password)
//            _loginLiveData.value = loginResponse.isSuccessful
//            loginResponse.headers()
//        } catch (e: Exception) {
//            Log.e("LoginViewModel", "Error: ${e.message}", e)
//            _loginLiveData.value = false
//        }
//
//    }