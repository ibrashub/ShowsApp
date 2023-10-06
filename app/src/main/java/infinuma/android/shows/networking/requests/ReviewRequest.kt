package infinuma.android.shows.networking.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewRequest(
    @SerialName("rating") val rating: Int,
    @SerialName("comment") val comment: String?,
    @SerialName("show_id") val show_id: Int,
    )
