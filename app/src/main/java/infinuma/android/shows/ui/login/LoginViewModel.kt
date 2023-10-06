package infinuma.android.shows.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import infinuma.android.shows.networking.requests.LoginRequest
import infinuma.android.shows.networking.responses.LoginResponse
import infinuma.android.shows.networking.ApiModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private var accessToken: String = ""
    private var client: String = ""
    private var uid: String = ""
    private var userId: String = ""
    private var imageUrl: String = ""

    private val _loginLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val loginLiveData: LiveData<Boolean> = _loginLiveData

    fun onLoginButtonClicked(username: String, password: String) {
        viewModelScope.launch {
            try {
                val response = login(username, password)
                if (response.isSuccessful) {

                    accessToken = response.headers()["access-token"] ?: ""
                    client = response.headers()["client"] ?: ""
                    uid = response.headers()["uid"] ?: ""
                    userId = response.headers()["userid"] ?: ""
                    imageUrl = response.headers()["imageUrl"] ?: ""
                    _loginLiveData.value = true

                    ApiModule.setAuthHeaders(accessToken, client, uid)
                } else {
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