package infinuma.android.shows.data.model.db_shows

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ShowEntity::class
    ],
    version = 2
)
abstract class ShowDatabase : RoomDatabase() {

    companion object {

        @Volatile
        private var INSTANCE: ShowDatabase? = null

        fun getDatabase(context: Context): ShowDatabase {
            return INSTANCE ?: synchronized(this) {
                val database = Room.databaseBuilder(
                    context = context,
                    klass = ShowDatabase::class.java,
                    name = "show_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                database
            }
        }
    }

    abstract fun showDao(): ShowDao
}