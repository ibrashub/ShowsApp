package infinuma.android.shows.networking

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
interface FileUploadService {
    @Multipart
    @POST("upload/photo")
    suspend fun uploadPhoto(@Part file: MultipartBody.Part): Response<PhotoUploadResponse>
}

@Serializable
data class PhotoUploadResponse (
    @SerialName("user") val user: ResponseUser
)

@Serializable
    data class ResponseUser (
    @SerialName("id") val id: Int,
    @SerialName("email") val email: String,
    @SerialName("image_url") var imageUrl: String
)