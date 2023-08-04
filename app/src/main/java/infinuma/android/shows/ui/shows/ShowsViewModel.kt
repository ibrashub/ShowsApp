package infinuma.android.shows.ui.shows

import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import infinuma.android.shows.data.model.db_shows.ShowDB
import infinuma.android.shows.data.model.db_shows.ShowDatabase
import infinuma.android.shows.networking.ApiModule
import infinuma.android.shows.networking.responses.Show
import infinuma.android.shows.networking.responses.User
import java.io.File
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class ShowsViewModel(val database: ShowDatabase) : ViewModel() {

    private val _showsLiveData: MutableLiveData<List<Show>> by lazy {
        MutableLiveData<List<Show>>()
    }

    val showsDBLiveData: LiveData<List<ShowDB>> = database.showDao().getAllShows().map { listOfShowEntities ->
        return@map listOfShowEntities.map { showEntity ->
            ShowDB(
                name = showEntity.name,
                //                id = 0,
                //                averageRating = 0f,
                description = showEntity.description,
                image = showEntity.imageResourceId
                //                numberOfReviews = 0,
                //                title = showEntity.name,
            )
        }
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var currentUser: User
    private fun fetchShows() {
        //        viewModelScope.launch {
        //            try {
        //                val response = ApiModule.retrofit.getShows()
        //                Log.d("ShowsViewModel", "API Response: ${response.body()}")
        //                if (response.isSuccessful) {
        //                    val showsResponse = response.body()
        //                    val shows = showsResponse?.shows ?: emptyList()
        //                    Log.d("ShowsViewModel", "Fetched shows: $shows")
        //                    //_showsLiveData.value = shows
        //                }
        //            } catch (e: Exception) {
        //                Log.e("ShowsViewModel", "Network Error: ${e.message}", e)
        //            }
        //        }
    }

    fun setCurrentUser(user: User) {
        currentUser = user
    }

    private fun uploadProfilePhoto(uri: Uri) {
        val file = File(uri.path)
        val requestBody = file.asRequestBody("image/*".toMediaType())
        val part = MultipartBody.Part.createFormData("photo", file.name, requestBody)

        viewModelScope.launch {
            try {
                val response = ApiModule.retrofit.uploadProfilePhoto(part)
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    val photoUrl = userResponse?.user?.imageUrl ?: ""
                    updateUserProfile(currentUser, photoUrl)

                    photoUrl.let {
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
        sharedPreferences.edit { putString("user", userJson) }
    }

    fun onProfilePhotoSelected(uri: Uri) {
        uploadProfilePhoto(uri)
    }

    init {
        fetchShows()
    }

}