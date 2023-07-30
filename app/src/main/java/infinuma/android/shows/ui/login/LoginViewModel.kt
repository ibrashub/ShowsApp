package infinuma.android.shows.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import infinuma.android.shows.data.model.LoginRequest
import infinuma.android.shows.networking.ApiModule
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _loginLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val loginLiveData: LiveData<Boolean> = _loginLiveData

    fun onLoginButtonClicked(username: String, password: String) = viewModelScope.launch {
        try {
            val loginResponse = login(username, password)
            _loginLiveData.value = loginResponse.isSuccessful
            loginResponse.headers()
        } catch (e: Exception) {
            Log.e("LoginViewModel", "Error: ${e.message}", e)
            _loginLiveData.value = false
        }

    }

    private suspend fun login(username: String, password: String) =
        ApiModule.retrofit.login(
            request = LoginRequest(
                email = username,
                password = password
            )
        )

}