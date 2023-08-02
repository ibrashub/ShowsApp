package infinuma.android.shows.networking.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShowDetailsResponse(
    @SerialName("show")
    val show: ShowDetails
)

@Serializable
data class ShowDetails(
    @SerialName("id") val id: String,
    @SerialName("average_rating") val averageRating: Float?,
    @SerialName("description") val description: String?,
    @SerialName("image_url") val imageUrl: String,
    @SerialName("no_of_reviews") val numberOfReviews: Int,
    @SerialName("title") val title: String
)

