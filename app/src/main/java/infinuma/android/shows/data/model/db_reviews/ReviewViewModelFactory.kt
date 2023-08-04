package infinuma.android.shows.data.model.db_reviews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import infinuma.android.shows.ui.shows.ShowDetailsViewModel
import infinuma.android.shows.ui.shows.ShowsViewModel

class ReviewViewModelFactory(val database: ReviewDatabase) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(ShowDetailsViewModel::class.java)) {
            return ShowDetailsViewModel(database) as T
        }
        throw IllegalStateException("The provided class is not ShowDetailsViewModel class")
    }
}