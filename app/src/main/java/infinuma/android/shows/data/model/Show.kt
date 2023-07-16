package infinuma.android.shows.data.model

import androidx.annotation.DrawableRes

data class Show(
    val name: String,
    val description: String,
    @DrawableRes val imageResourceId: Int
)
