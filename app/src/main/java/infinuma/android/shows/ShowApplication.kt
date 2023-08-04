package infinuma.android.shows

import android.app.Application
import infinuma.android.shows.data.model.db_shows.ShowDatabase
import infinuma.android.shows.data.model.db_shows.ShowEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import infinuma.android.shows.data.model.db_reviews.ReviewDatabase
import infinuma.android.shows.data.model.db_reviews.ReviewEntity

class ShowApplication : Application() {

    private val databaseShow by lazy {
        ShowDatabase.getDatabase(this)
    }

    private val databaseReview by lazy {
        ReviewDatabase.getDatabase(this)
    }

    private val shows = listOf(
        ShowEntity("1", "The Office", "A random 123 description 1", R.drawable.ic_office),
        ShowEntity("2", "Stranger Things", "A random show description 2", R.drawable.ic_stranger_things),
        ShowEntity("3", "Krv Nije Voda", "A random show description 3", R.drawable.krv_nije_voda),
        ShowEntity("4", "Test Show", "A random show description 3", R.drawable.placeholder_image),
        ShowEntity("5", "Test Show2", "A random show description 3", R.drawable.placeholder_image),
    )

    private val reviews = listOf(
        ReviewEntity("1", "john", 5, "Very good show. 10/10", R.drawable.ic_profile_placeholder),
        ReviewEntity("2", "jack", 4, "Very nice show, should watch it ASAP", R.drawable.ic_profile_placeholder),
        ReviewEntity("3", "johns", 3, "Very nice show", R.drawable.ic_profile_placeholder),
        ReviewEntity("4", "joe", 2, "Very good show, I like it!", R.drawable.ic_profile_placeholder),
        ReviewEntity("5", "jeff", 1, "Very bad show, not recommended", R.drawable.ic_profile_placeholder),
        ReviewEntity("6", "james", 3, "Very good show it was", R.drawable.ic_profile_placeholder),
        ReviewEntity("7", "jamie", 5, "An awesome show totally worth it", R.drawable.ic_profile_placeholder),
    )

    override fun onCreate() {
        super.onCreate()
        GlobalScope.launch {

            databaseShow.showDao().insertAllShows(shows)
            databaseReview.reviewDao().insertAllReviews(reviews = reviews)
        }
    }
}