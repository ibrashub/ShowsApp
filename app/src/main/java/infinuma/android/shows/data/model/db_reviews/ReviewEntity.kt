package infinuma.android.shows.data.model.db_reviews

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "review")
data class ReviewEntity(
    @ColumnInfo(name = "id") @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "rating") val rating: Int,
    @ColumnInfo(name = "comment") val comment: String,
    @ColumnInfo(name = "image") @DrawableRes val imageResourceId: Int
)
