package infinuma.android.shows.networking

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response

private const val TOKEN_TYPE = "token-type"
private const val BEARER = "Bearer"
private const val ACCESS_TOKEN = "access-token"
private const val CLIENT = "client"
private const val UID = "uid"
private const val USER_IMAGE_URL = "user-image-url"
private const val USER_ID = "user-id"

class AuthInterceptor(private val sharedPreferences: SharedPreferences) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = sharedPreferences.getString(ACCESS_TOKEN, "") ?: ""
        val client = sharedPreferences.getString(CLIENT, "") ?: ""
        val uid = sharedPreferences.getString(UID, "") ?: ""
        val userImageUrl = sharedPreferences.getString(USER_IMAGE_URL, "") ?: ""
        val userId = sharedPreferences.getString(USER_ID, "") ?: ""

        val originalRequest = chain.request()
        val authenticatedRequest = originalRequest.newBuilder()
            .header(TOKEN_TYPE, BEARER)
            .header(ACCESS_TOKEN, accessToken)
            .header(CLIENT, client)
            .header(UID, uid)
            .header(USER_IMAGE_URL, userImageUrl)
            .header(USER_ID, userId)
            .build()
        return chain.proceed(authenticatedRequest)
    }
}
