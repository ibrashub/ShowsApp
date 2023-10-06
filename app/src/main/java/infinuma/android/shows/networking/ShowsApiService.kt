package infinuma.android.shows.networking

import infinuma.android.shows.networking.requests.LoginRequest
import infinuma.android.shows.networking.requests.RegisterRequest
import infinuma.android.shows.networking.requests.ReviewRequest
import infinuma.android.shows.networking.responses.GetReviewsResponse
import infinuma.android.shows.networking.responses.LoginResponse
import infinuma.android.shows.networking.responses.RegisterResponse
import infinuma.android.shows.networking.responses.ReviewResponse
import infinuma.android.shows.networking.responses.ShowDetailsResponse
import infinuma.android.shows.networking.responses.ShowsResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
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
        @Part image: MultipartBody.Part
    ): Response<PhotoUploadResponse>

    @POST("/reviews")
    suspend fun createReview(@Body request: ReviewRequest): Response<ReviewResponse>

    @GET("/shows/{showId}/reviews")
    suspend fun getReviews(@Path("showId") showId: Int): Response<GetReviewsResponse>

    @GET("shows")
    suspend fun getShows(): Response<ShowsResponse>

    @GET("shows/{id}")
    suspend fun getShowDetails(@Path("id") showId: Int): Response<ShowDetailsResponse>


}