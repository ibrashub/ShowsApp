package infinuma.android.shows.ui.shows

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import infinuma.android.shows.data.model.Review
import infinuma.android.shows.data.model.ReviewResponse
import infinuma.android.shows.data.model.ShowDetails
import infinuma.android.shows.data.model.User
import infinuma.android.shows.networking.ApiModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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

    private val _reviewCreateResponseLiveData = MutableLiveData<ReviewResponse>()
    val reviewCreateResponseLiveData: LiveData<ReviewResponse> = _reviewCreateResponseLiveData

    private val _showDetailsLiveData = MutableLiveData<ShowDetails>()
    val showDetailsLiveData: LiveData<ShowDetails> = _showDetailsLiveData

    fun fetchShowDetails(showId: Int) {
        viewModelScope.launch {
            try {
                val response = ApiModule.retrofit.getShowDetails(showId)
                if (response.isSuccessful) {
                    val showDetailsResponse = response.body()
                    val showDetails = showDetailsResponse?.show
                    showDetails?.let {
                        _showDetailsLiveData.value = it
                    }
                } else {
                    Log.e("ViewModel", "Unable to fetch show details. Response code: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Error fetching show details: ${e.message}")
            }
        }
    }

    fun fetchReviews(showId: Int) {
        viewModelScope.launch {
            try {
                val response = ApiModule.retrofit.getReviews(showId)
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

    private suspend fun getReviews(showId: Int): Response<ReviewResponse> {
        return ApiModule.retrofit.getReviews(showId)
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

    fun addNewReviewToList(rating: Int, comment: String, userId: String, userEmail: String, userImageUrl: String) {
        val currentReviews = reviewsLiveData.value?.toMutableList() ?: mutableListOf()
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
        currentReviews.add(review)
        _reviewsLiveData.value = currentReviews.toList()

        viewModelScope.launch {
            try {
                val response = ApiModule.retrofit.createReview(review)
                if (response.isSuccessful) {
                    val reviewResponse = response.body()
                    reviewResponse?.let {
                        // Update the LiveData with the new review response
                        _reviewCreateResponseLiveData.value = it
                    }
                } else {
                    // Handle API error here (e.g., show an error message)
                    Log.e("ViewModel", "Error creating review. Response code: ${response.code()}")
                }
            } catch (e: Exception) {
                // Handle network error here (e.g., show an error message)
                Log.e("ViewModel", "Error creating review: ${e.message}")
            }
        }
    }
    //    private fun getResourceName(resourceId: Int): String {
    //        return try {
    //            // Get the resource name as a string
    //            val context = getApplication<Application>().applicationContext
    //            context?.resources?.getResourceName(resourceId) ?: ""
    //
    //        } catch (e: Exception) {
    //            Log.e("ViewModel", "Error getting resource name: ${e.message}")
    //            ""
    //        }
    //    }
}