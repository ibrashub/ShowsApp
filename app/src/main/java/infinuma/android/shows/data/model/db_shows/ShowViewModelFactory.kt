package infinuma.android.shows.data.model.db_shows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import infinuma.android.shows.ui.shows.ShowsViewModel

class ShowViewModelFactory(val database: ShowDatabase) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(ShowsViewModel::class.java)) {
            return ShowsViewModel(database) as T
        }
        throw IllegalStateException("The provided class is not ShowViewModel class")
    }
}