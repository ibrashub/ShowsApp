package infinuma.android.shows.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewResponse(
    @SerialName("reviews") val reviews: List<Review>,
    @SerialName("meta") val meta: Meta,
    @SerialName("pagination") val pagination: Pagination
)

@Serializable
data class Review(
    @SerialName("id") val id: String,
    @SerialName("comment") val comment: String,
    @SerialName("rating") val rating: Int,
    @SerialName("show_id") val showId: String,
    @SerialName("user") val user: User
)

@Serializable
data class User(
    @SerialName("id") val id: String,
    @SerialName("email") val email: String,
    @SerialName("image_url") val imageUrl: String?
)
