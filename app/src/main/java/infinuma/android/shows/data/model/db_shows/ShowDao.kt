package infinuma.android.shows.data.model.db_shows

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ShowDao {

    @Query("SELECT * FROM show")
    fun getAllShows(): LiveData<List<ShowEntity>>

    @Query("SELECT * FROM show WHERE id IS :showId")
    suspend fun getShow(showId: String): ShowEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllShows(shows: List<ShowEntity>)
}