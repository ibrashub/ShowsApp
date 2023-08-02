package infinuma.android.shows.data.model

import kotlinx.serialization.SerialName

data class UserResponse(
    val user: User
)

    data class User(
@SerialName("id") val id: String,
@SerialName("client") val client: String,
@SerialName("access-token") val accessToken: String,
@SerialName("email") val email: String,
@SerialName("image_url") var imageUrl: String
)

