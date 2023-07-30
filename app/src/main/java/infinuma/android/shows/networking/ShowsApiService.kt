package infinuma.android.shows.networking

import com.chuckerteam.chucker.api.ChuckerInterceptor
import infinuma.android.shows.data.model.LoginRequest
import infinuma.android.shows.data.model.LoginResponse
import infinuma.android.shows.data.model.RegisterRequest
import infinuma.android.shows.data.model.RegisterResponse
import infinuma.android.shows.data.model.Show
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ShowsApiService {

    @POST("users")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("users/sign_in")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("api/shows")
    fun getShows(@Header("Authorization") authToken: String?): Call<List<Show?>?>?

}