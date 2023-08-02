package infinuma.android.shows.networking.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class GetReviewsResponse(
    @SerialName("reviews") val reviews: List<Review>,
)
