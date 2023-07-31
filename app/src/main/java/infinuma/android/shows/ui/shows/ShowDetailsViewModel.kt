package infinuma.android.shows.ui.shows

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import infinuma.android.shows.data.model.Review
import infinuma.android.shows.data.model.ReviewResponse
import infinuma.android.shows.data.model.User
import infinuma.android.shows.networking.ApiModule
import kotlinx.coroutines.launch
import retrofit2.Response

class ShowDetailsViewModel : ViewModel() {

    private val _descriptionLiveData = MutableLiveData<String>()
    val descriptionLiveData: LiveData<String> = _descriptionLiveData

    private val _imageUrlLiveData = MutableLiveData<String>()
    val imageUrlLiveData: LiveData<String> = _imageUrlLiveData

    private val _reviewsLiveData: MutableLiveData<List<Review>> by lazy {
        MutableLiveData<List<Review>>()
    }
    val reviewsLiveData: LiveData<List<Review>> = _reviewsLiveData

    private val _ratingTextViewLiveData = MutableLiveData<Pair<Int, Float>>()
    val ratingTextViewLiveData: LiveData<Pair<Int, Float>> = _ratingTextViewLiveData

    fun fetchReviews(showId: String) {
        viewModelScope.launch {
            try {
                val response = getReviews(showId)
                if (response.isSuccessful) {
                    val reviewResponse = response.body()
                    reviewResponse?.let {
                        _reviewsLiveData.value = it.reviews
                    }
                } else {
                    Log.e("ViewModel", "Unable to fetch reviews. Response code: ${response.code()}")
                }
            } catch (e: Exception) {
            Log.e("ViewModel", "Error fetching reviews: ${e.message}")
            }
        }
    }

    private suspend fun getReviews(showId: String): Response<ReviewResponse> {
        return ApiModule.retrofit.getReviews(showId)
    }

    fun populateShowData(description: String, imageResourceId: String) {
        _descriptionLiveData.value = description
        _imageUrlLiveData.value = imageResourceId
    }

    fun calculateAverageRating(): Pair<Int, Float> {
        var totalRating = 0

        val reviews = reviewsLiveData.value ?: emptyList()

        for (review in reviews) {
            totalRating += review.rating
        }

        val averageRating = if (reviews.isNotEmpty()) {
            reviews.sumOf { it.rating }.toFloat() / reviews.size
        } else {
            0f
        }

        _ratingTextViewLiveData.value = Pair(reviews.size, averageRating)
        return Pair(totalRating, averageRating)
    }

    fun addNewReviewToList(rating: Int, comment: String, userId: String, userEmail: String, userImageUrl: String?) {
        val updatedReviews = reviewsLiveData.value?.toMutableList() ?: mutableListOf()
        val review = Review(
            id = "",
            comment = comment,
            rating = rating,
            showId = "",
            user = User(
                id = userId,
                email = userEmail,
                imageUrl = userImageUrl
            )
        )
        updatedReviews.add(review)
        _reviewsLiveData.value = updatedReviews.toList()
    }
}
