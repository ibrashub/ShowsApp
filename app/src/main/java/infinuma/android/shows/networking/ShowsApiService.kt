package infinuma.android.shows.networking

import infinuma.android.shows.data.model.LoginRequest
import infinuma.android.shows.data.model.LoginResponse
import infinuma.android.shows.data.model.RegisterRequest
import infinuma.android.shows.data.model.RegisterResponse
import infinuma.android.shows.networking.ApiModule.retrofit
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ShowsApiService {

    @POST("users")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("users")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

}