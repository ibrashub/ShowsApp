package infinuma.android.shows.data.model.db_shows

import androidx.annotation.DrawableRes

data class ShowDB(
    val name: String,
    val description: String,
    @DrawableRes val image: Int
)
