package infinuma.android.shows.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import infinuma.android.shows.networking.requests.RegisterRequest
import infinuma.android.shows.networking.ApiModule
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {

    private val _registrationResultLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val registrationResultLiveData: LiveData<Boolean> = _registrationResultLiveData

    fun onRegisterButtonClicked(username: String, password: String) = viewModelScope.launch {
        try {
            val registerResponse = register(username, password, password)
            _registrationResultLiveData.value = registerResponse.isSuccessful
            registerResponse.headers()
        } catch (e: Exception) {
            Log.e("RegistrationViewModel", "Error: ${e.message}", e)
            _registrationResultLiveData.value = false
        }

    }

    private suspend fun register(username: String, password: String, passwordConfirmation: String) =
        ApiModule.retrofit.register(
            request = RegisterRequest(
                email = username,
                password = password,
                passwordConfirmation = passwordConfirmation
            )
        )

}