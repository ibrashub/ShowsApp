package infinuma.android.shows.networking

import infinuma.android.shows.data.model.LoginRequest
import infinuma.android.shows.data.model.LoginResponse
import infinuma.android.shows.data.model.RegisterRequest
import infinuma.android.shows.data.model.RegisterResponse
import infinuma.android.shows.data.model.Review
import infinuma.android.shows.data.model.ReviewResponse
import infinuma.android.shows.data.model.ShowDetailsResponse
import infinuma.android.shows.data.model.ShowsResponse
import infinuma.android.shows.data.model.UserResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ShowsApiService {

    @POST("users")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("users/sign_in")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @Multipart
    @PUT("users")
    suspend fun uploadProfilePhoto(
        @Part image: MultipartBody.Part,
        @Header("uid") uid: String,
        @Header("client") client: String,
        @Header("access-token") accessToken: String
    ): Response<UserResponse>

    @GET("shows")
    suspend fun getShows(): Response<ShowsResponse>

    @GET("shows/{id}")
    suspend fun getShowDetails(@Path("id") showId: Int): Response<ShowDetailsResponse>


    @POST("/reviews")
    suspend fun createReview(@Body review: Review): Response<ReviewResponse>

    @GET("/shows/{showId}/reviews")
    suspend fun getReviews(@Path("showId") showId: Int): Response<ReviewResponse>


}