package infinuma.android.shows.data.model.db_reviews

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ReviewEntity::class
    ],
    version = 1
)
abstract class ReviewDatabase : RoomDatabase() {

    companion object {

        @Volatile
        private var INSTANCE: ReviewDatabase? = null

        fun getDatabase(context: Context): ReviewDatabase {
            return INSTANCE ?: synchronized(this) {
                val database = Room.databaseBuilder(
                    context = context,
                    klass = ReviewDatabase::class.java,
                    name = "review_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                database
            }
        }
    }

    abstract fun reviewDao(): ReviewDao
}