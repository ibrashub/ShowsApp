package infinuma.android.shows.data.model.db_reviews

import androidx.annotation.DrawableRes

data class ReviewDB(
    val email: String,
    val comment: String,
    val rating: Int,
    @DrawableRes val image: Int
)
