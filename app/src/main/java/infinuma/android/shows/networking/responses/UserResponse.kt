package infinuma.android.shows.networking.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
@SerialName("id") val id: String,
@SerialName("client") val client: String,
@SerialName("access-token") val accessToken: String,
@SerialName("email") val email: String,
@SerialName("image_url") var imageUrl: String
)

