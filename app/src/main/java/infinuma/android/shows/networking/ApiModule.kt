package infinuma.android.shows.networking

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import infinuma.android.shows.ui.login.REMEMBER_ME
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object ApiModule {
    private const val ACCESS_TOKEN = "access-token"
    private const val CLIENT = "client"
    private const val UID = "uid"
    private const val BASE_URL = "https://tv-shows.infinum.academy/"
    private val json = Json { ignoreUnknownKeys = true }
    lateinit var retrofit: ShowsApiService
    private lateinit var sharedPreferences: SharedPreferences


    fun initRetrofit(context: Context) {
        sharedPreferences = context.getSharedPreferences(REMEMBER_ME, Context.MODE_PRIVATE)
        val okhttp = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(ChuckerInterceptor.Builder(context).build())
            .addInterceptor { chain ->
                val request = chain.request()
                val response = chain.proceed(request)
                response
            }
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(okhttp)
            .build()
            .create(ShowsApiService::class.java)
    }

    fun setAuthHeaders(accessToken: String, client: String, uid: String) {
        sharedPreferences.edit {
            putString(ACCESS_TOKEN, accessToken)
            putString(CLIENT, client)
            putString(UID, uid)
        }
        val authInterceptor = AuthInterceptor(sharedPreferences)
        val okhttp = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(okhttp)
            .build()
            .create(ShowsApiService::class.java)
    }


}