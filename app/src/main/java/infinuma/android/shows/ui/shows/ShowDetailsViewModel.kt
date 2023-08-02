package infinuma.android.shows.ui.shows

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import infinuma.android.shows.data.model.Review
import infinuma.android.shows.data.model.ReviewRequest
import infinuma.android.shows.data.model.ReviewResponse
import infinuma.android.shows.data.model.ShowDetails
import infinuma.android.shows.networking.ApiModule
import kotlinx.coroutines.launch

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
                    val getReviewsResponse = response.body()
                    val review = getReviewsResponse?.reviews
                    review?.let {
                        _reviewsLiveData.value = it
                    }
                } else {
                    Log.e("ViewModel", "Unable to fetch reviews. Response code: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Error fetching reviews: ${e.message}")
            }
        }
    }

    fun addNewReviewToList(comment: String? = null, rating: Int, showId: Int) = viewModelScope.launch {

        try {
            val response = createReview(comment, rating, showId)
            if (response.isSuccessful) {
                val reviewResponse = response.body()
                reviewResponse?.let {
                    _reviewCreateResponseLiveData.value = it
                }
            } else {
                Log.e("ViewModel", "Error creating review. Response code: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("ViewModel", "Error creating review: ${e.message}")
        }
    }
}

private suspend fun createReview(comment: String? = null, rating: Int, showId: Int) =
    ApiModule.retrofit.createReview(
        request = ReviewRequest(
            comment = comment,
            rating = rating,
            show_id = showId
        )
    )
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
