package infinuma.android.shows.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import infinuma.android.shows.data.model.RegisterRequest
import infinuma.android.shows.networking.ApiModule
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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

    fun coroutineDemo() {
        val job = viewModelScope.launch(Dispatchers.IO) {
            Log.e("Coroutine", "This is executed in $coroutineContext")
            try {
                writeEverySecond()
            } catch (e: CancellationException) {
                Log.e("Coroutine", "Canceled")
            } catch (e: IllegalStateException) {
                Log.e("Coroutine", "Error handled")
            }

        }
        viewModelScope.launch {
            delay(10000)
            job.cancel()
        }
    }

    private suspend fun writeEverySecond() {
        var timer = 0
        while (true) {
            Log.e("Coroutine", "The timer value ${timer++}")
            delay(2000)
            if (timer == 2) throw IllegalStateException()
        }
    }

}