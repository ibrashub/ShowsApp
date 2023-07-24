import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import infinuma.android.shows.data.model.Show
import infinuma.android.shows.data.model.Review
import infinuma.android.shows.R


class ShowDetailsViewModel : ViewModel() {

    private val reviews = listOf(
        Review(1, 5, "Ivan", "Best show ever!!!", R.drawable.ic_profile_placeholder),
        Review(2, 2, "Mark", "Its fine", R.drawable.ic_profile_placeholder),
        Review(3, 1, "Ivan", "WORST!", R.drawable.ic_profile_placeholder),
        Review(4, 1, "User1231212", "Best show ever!!!", R.drawable.ic_profile_placeholder),
        Review(5, 3, "User123123212", "Longer comment for testing purposes of the recyclerview", R.drawable.ic_profile_placeholder),
        Review(6, 2, "User121771212", "haha!", R.drawable.ic_profile_placeholder),
    )

    private val _showDetailsLiveData = MutableLiveData<Show>()
    val showDetailsLiveData: LiveData<Show> = _showDetailsLiveData

    private val _reviewsLiveData = MutableLiveData<List<Review>>()
    val reviewsLiveData: LiveData<List<Review>> = _reviewsLiveData

    private val _addNewReviewsLiveData = MutableLiveData<List<Review>>()
    val addNewReviewsLiveData: LiveData<List<Review>> = _addNewReviewsLiveData

    private val _descriptionLiveData = MutableLiveData<String>()
    val descriptionLiveData: LiveData<String> = _descriptionLiveData

    private val _imageResourceIdLiveData = MutableLiveData<Int>()
    val imageResourceIdLiveData: LiveData<Int> = _imageResourceIdLiveData

    private val _averageRatingLiveData = MutableLiveData<Float>()
    val averageRatingLiveData: LiveData<Float> = _averageRatingLiveData


    init {
        _reviewsLiveData.value = reviews
    }

    fun populateShowData(description: String, imageResourceId: Int) {
        _descriptionLiveData.value = description
        _imageResourceIdLiveData.value = imageResourceId
    }

    private fun calculateAverageRating(reviews: List<Review>) {
        if (reviews.isEmpty()) {
            _averageRatingLiveData.value = 0f
            return
        }

        val totalRating = reviews.sumOf { it.rating }
        val averageRating = totalRating.toFloat() / reviews.size
        _averageRatingLiveData.value = averageRating
    }

     fun addNewReviewToList(rating: Int, comment: String) {
        val review = Review(reviews.size + 1, rating, "username", comment, R.drawable.ic_profile_placeholder)
        val updatedReviews = reviews.toMutableList()
         updatedReviews.add(review)
        _reviewsLiveData.value = updatedReviews

    }


}
