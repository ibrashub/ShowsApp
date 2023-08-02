package infinuma.android.shows.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewResponse(
    @SerialName("review") val review: Review,
)

@Serializable
data class Review(
    @SerialName("id") val id: String,
    @SerialName("comment") val comment: String?,
    @SerialName("rating") val rating: Int,
    @SerialName("show_id") val showId: Int,
    @SerialName("user") val user: ReviewUser
)

@Serializable
data class ReviewUser(
    @SerialName("id") val id: String,
    @SerialName("email") val email: String,
    @SerialName("image_url") var imageUrl: String?
)
