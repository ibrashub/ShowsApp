package infinuma.android.shows

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import infinuma.android.shows.data.model.Review
import infinuma.android.shows.data.model.reviews
import infinuma.android.shows.databinding.ActivityShowDetailsBinding
import infinuma.android.shows.ui.login.ReviewsAdapter

class ShowDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowDetailsBinding
    private lateinit var adapter: ReviewsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        initReviewsRecycler()

        initAddReview()

        binding.reviewButton.setOnClickListener {
            showAlertDialog()
        }

        val showName = intent.getStringExtra("showName")
        val showDescription = intent.getStringExtra("showDescription")
        val showImage = intent.getIntExtra("showImage", 0)

        populateShowDetails(showDescription, showImage)
        supportActionBar?.title = showName


    }

    private fun populateShowDetails(description: String?, imageResourceId: Int) {
        binding.showDescriptionTextView.text = description
        binding.showImageView.setImageResource(imageResourceId)
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder
            .setTitle((R.string.app_name))
            .setMessage("Are you sure you want to add a new review?")
            .setPositiveButton(R.string.add) { _, _ ->
                addNewReviewToList(Review(5, 5, "username", "new review", R.drawable.ic_profile_placeholder))
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }

        builder.show()
    }

    private fun initReviewsRecycler() {
        adapter = ReviewsAdapter(reviews) { review ->
            val intent = Intent(this, ShowDetailsActivity::class.java)
            intent.putExtra("reviewId", review.id)
            intent.putExtra("reviewRating", review.rating)
            intent.putExtra("reviewName", review.name)
            intent.putExtra("reviewComment", review.comment)
            intent.putExtra("reviewImage", review.imageResourceId)
            startActivity(intent)
        }
        binding.reviewsRecycler.layoutManager = LinearLayoutManager(this)

        binding.reviewsRecycler.adapter = adapter

        binding.reviewsRecycler.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        val (totalRatings, averageRating) = calculateAverageRating(reviews)

        val formattedText = "%d ratings, %.2f average".format(totalRatings, averageRating)

        binding.ratingTextView.text = formattedText

        binding.averageRatingBar.rating = averageRating


    }

    private fun calculateAverageRating(reviews: List<Review>): Pair<Int, Float> {
        if (reviews.isEmpty()) {
            binding.ratingTextView.visibility = View.GONE
            binding.averageRatingBar.visibility = View.GONE
            binding.reviewsRecycler.visibility = View.GONE
            binding.noReviewsTextView.visibility = View.VISIBLE
        }

        var totalRating = 0

        for (review in reviews) {
            totalRating += review.rating
        }

        val averageRating = totalRating.toFloat() / reviews.size
        return Pair(reviews.size, averageRating)
    }

    private fun initAddReview() {

        binding.reviewButton.setOnClickListener {
            addNewReviewToList(Review(7, 5, "username", "comment", R.drawable.ic_profile_placeholder))
        }
    }

    private fun addNewReviewToList(item: Review) {
        adapter.addItem(item)
        //        adapter.notifyItemInserted(reviews.size)
        //        calculateAverageRating(reviews)
    }

}


