//package infinuma.android.shows.data.model
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import infinuma.android.shows.networking.ApiModule
//import infinuma.android.shows.networking.ShowsApiService
//import java.io.File
//import kotlinx.coroutines.launch
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.MultipartBody
//import okhttp3.RequestBody
//import okhttp3.RequestBody.Companion.asRequestBody
//
//class UserViewModel : ViewModel() {
//    private val apiService: ShowsApiService = ApiModule.retrofit.create(ShowsApiService::class.java)
//
//    private val _userLiveData = MutableLiveData<User>()
//    val userLiveData: LiveData<User> = _userLiveData
//
//    fun uploadProfilePhoto(imageFile: File, uid: String, client: String, accessToken: String) {
//        viewModelScope.launch {
//            try {
//                val requestFile: RequestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
//                val image: MultipartBody.Part = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)
//
//                val response = apiService.uploadProfilePhoto(image, uid, client, accessToken)
//                if (response.isSuccessful) {
//                    val userResponse = response.body()
//                    val user = userResponse?.user
//                    user?.let {
//                        _userLiveData.value = it
//                    }
//                } else {
//                    // Handle API error
//                }
//            } catch (e: Exception) {
//                // Handle network error
//            }
//        }
//    }
//}
