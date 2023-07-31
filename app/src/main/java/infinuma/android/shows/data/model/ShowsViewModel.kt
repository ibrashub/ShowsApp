package infinuma.android.shows.data.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import infinuma.android.shows.R
import infinuma.android.shows.networking.ApiModule
import kotlinx.coroutines.launch

class ShowsViewModel : ViewModel() {

    private val _showsLiveData: MutableLiveData<List<Show>> = MutableLiveData()
    val showsLiveData: LiveData<List<Show>> = _showsLiveData

    fun fetchShows() {
        viewModelScope.launch {
            try {
                val response = ApiModule.retrofit.getShows()
                Log.d("ShowsViewModel", "API Response: ${response.body()}")
                if (response.isSuccessful) {
                    val showsResponse = response.body()
                    val shows = showsResponse?.shows ?: emptyList()
                    Log.d("ShowsViewModel", "Fetched shows: $shows")
                    _showsLiveData.value = shows
                }
            } catch (e: Exception) {
                Log.e("ShowsViewModel", "Network Error: ${e.message}", e)
            }
        }
    }

}