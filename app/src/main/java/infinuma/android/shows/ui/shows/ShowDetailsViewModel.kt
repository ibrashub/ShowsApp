package infinuma.android.shows.ui.shows
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import infinuma.android.shows.R
import infinuma.android.shows.data.model.Review

class ShowDetailsViewModel : ViewModel() {

    private val reviews = listOf(
        Review(1, 5, "Ivan", "Best show ever!!!", R.drawable.ic_profile_placeholder),
        Review(2, 3, "Mark", "An okay movie, not my favorite", R.drawable.ic_profile_placeholder),
        Review(3, 1, "Renato", "WORST!", R.drawable.ic_profile_placeholder),
        Review(4, 4, "User123", "Worth watching it", R.drawable.ic_profile_placeholder),
        Review(5, 3, "ibra", "An okay movie, not my favorite. Worth watching it", R.drawable.ic_profile_placeholder),
        Review(6, 4, "User999", "So funny haha!", R.drawable.ic_profile_placeholder),
    )

    private var idIncrement = reviews.size

    private val _descriptionLiveData = MutableLiveData<String>()
    val descriptionLiveData: LiveData<String> = _descriptionLiveData

    private val _imageResourceIdLiveData = MutableLiveData<Int>()
    val imageResourceIdLiveData: LiveData<Int> = _imageResourceIdLiveData

    private val _reviewsLiveData = MutableLiveData<List<Review>>()
    val reviewsLiveData: LiveData<List<Review>> = _reviewsLiveData

    private val _ratingTextViewLiveData = MutableLiveData<Pair<Int, Float>>()
    val ratingTextViewLiveData: LiveData<Pair<Int, Float>> = _ratingTextViewLiveData


    fun populateShowData(description: String, imageResourceId: Int) {
        _descriptionLiveData.value = description
        _imageResourceIdLiveData.value = imageResourceId
    }

    fun calculateAverageRating(): Pair<Int, Float> {
        var totalRating = 0

        for (review in reviews) {
            totalRating += review.rating
        }

        val averageRating = reviews.sumOf { it.rating }.toFloat() / reviews.size
        _ratingTextViewLiveData.value = Pair(reviews.size, averageRating)
        return Pair(totalRating, averageRating)
    }

    fun addNewReviewToList(rating: Int, comment: String) {
        idIncrement++
        val updatedReviews = reviews.toMutableList()
        val review = Review(idIncrement, rating, R.string.username.toString(), comment, R.drawable.ic_profile_placeholder)
        updatedReviews.add(review)
        _reviewsLiveData.value = updatedReviews.toList()
    }

    init {
        _reviewsLiveData.value = reviews
        _ratingTextViewLiveData.value = calculateAverageRating()
    }


}
