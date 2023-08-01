package infinuma.android.shows.networking

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FileUploadService {
    @Multipart
    @POST("upload/photo")
    suspend fun uploadPhoto(@Part file: MultipartBody.Part): Response<PhotoUploadResponse>
}

data class PhotoUploadResponse(val photoUrl: String)