package infinuma.android.shows

import android.app.Application
import infinuma.android.shows.data.model.db_shows.ShowDatabase
import infinuma.android.shows.data.model.db_shows.ShowEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import infinuma.android.shows.R

class ShowApplication : Application() {

    val database by lazy {
        ShowDatabase.getDatabase(this)
    }

    private val shows = listOf(
        ShowEntity("1", "The Office", "A random 123 description 1" ,R.drawable.ic_office),
        ShowEntity("2", "Stranger Things", "A random show description 2" ,R.drawable.ic_stranger_things),
        ShowEntity("3", "Krv Nije Voda", "A random show description 3" ,R.drawable.krv_nije_voda),
        ShowEntity("4", "Test Show", "A random show description 3" ,R.drawable.placeholder_image),
        ShowEntity("5", "Test Show2", "A random show description 3" ,R.drawable.placeholder_image),
    )

    private val reviews = listOf(
        ShowEntity("1", "The Office", "A random 123 description 1" ,R.drawable.ic_office),
        ShowEntity("2", "Stranger Things", "A random show description 2" ,R.drawable.ic_stranger_things),
        ShowEntity("3", "Krv Nije Voda", "A random show description 3" ,R.drawable.krv_nije_voda),
        ShowEntity("4", "Test Show", "A random show description 3" ,R.drawable.placeholder_image),
        ShowEntity("5", "Test Show2", "A random show description 3" ,R.drawable.placeholder_image),
    )

    override fun onCreate() {
        super.onCreate()
        GlobalScope.launch {

            database.showDao().insertAllShows(shows)
        }
    }
}