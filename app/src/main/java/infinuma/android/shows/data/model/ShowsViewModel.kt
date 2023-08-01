package infinuma.android.shows.data.model

import android.content.SharedPreferences
import android.net.Uri
import android.provider.Settings.Global.putString
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import infinuma.android.shows.networking.ApiModule
import infinuma.android.shows.networking.FileUploadService
import java.io.File
import infinuma.android.shows.data.model.UserResponse
import infinuma.android.shows.data.model.ReviewResponse
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://tv-shows.infinum.academy/"

class ShowsViewModel(private val sharedPreferences2: SharedPreferences) : ViewModel() {

    private val _showsLiveData: MutableLiveData<List<Show>> = MutableLiveData()
    val showsLiveData: LiveData<List<Show>> = _showsLiveData
    private lateinit var currentUser: User
    private fun fetchShows() {
        viewModelScope.launch {
            try {
                val response = ApiModule.retrofit.getShows()
                Log.d("ShowsViewModel", "API Response: ${response.body()}")
                if (response.isSuccessful) {
                    val showsResponse = response.body()
                    val shows = showsResponse?.shows ?: emptyList()
                    Log.d("ShowsViewModel", "Fetched shows: $shows")
                    _showsLiveData.value = shows
                }
            } catch (e: Exception) {
                Log.e("ShowsViewModel", "Network Error: ${e.message}", e)
            }
        }
    }

    private fun uploadProfilePhoto(uri: Uri) {
        val file = File(uri.path)
        val requestBody = file.asRequestBody("image/*".toMediaType())
        val part = MultipartBody.Part.createFormData("photo", file.name, requestBody)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val fileUploadService = retrofit.create(FileUploadService::class.java)

        viewModelScope.launch {
            try {
                val response = fileUploadService.uploadPhoto(part)
                if (response.isSuccessful) {
                    val photoUrl = response.body()?.photoUrl ?: ""
                    updateUserProfile(currentUser, photoUrl)

                    photoUrl?.let {
                        currentUser.imageUrl = it
                    }
                } else {
                    Log.e("UploadPhoto", "Error uploading photo. Response code: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("UploadPhoto", "Error uploading photo: ${e.message}")
            }
        }
    }

    private fun updateUserProfile(user: User, imageUrl: String) {
        user.imageUrl = imageUrl
        val gson = Gson()
        val userJson = gson.toJson(user)
        sharedPreferences2.edit { putString("user", userJson) }
    }

    init {
        fetchShows()
    }

}