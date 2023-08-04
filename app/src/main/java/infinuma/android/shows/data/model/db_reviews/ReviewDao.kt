package infinuma.android.shows.data.model.db_reviews

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ReviewDao {

    @Query("SELECT * FROM review")
    fun getAllReviews(): LiveData<List<ReviewEntity>>

    @Query("SELECT * FROM review WHERE id IS :reviewId")
    suspend fun getReview(reviewId: String): ReviewEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllReviews(reviews: List<ReviewEntity>)
}