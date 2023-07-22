package infinuma.android.shows.ui.login

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import infinuma.android.shows.R
import infinuma.android.shows.data.model.Review
import infinuma.android.shows.data.model.reviews
import infinuma.android.shows.databinding.FragmentShowDetailsBinding

class ShowDetailsFragment : Fragment(R.layout.fragment_show_details) {

    private var _binding: FragmentShowDetailsBinding? = null
    private lateinit var adapter: ReviewsAdapter


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.toolbar.setNavigationOnClickListener {
//            requireActivity().onBackPressed()
//        }

        initReviewsRecycler()

        initAddReview()

        binding.reviewButton.setOnClickListener {
            showAlertDialog()
        }

        val showName = requireActivity().intent.getStringExtra("showName")
        val showDescription = requireActivity().intent.getStringExtra("showDescription")
        val showImage = requireActivity().intent.getIntExtra("showImage", 0)

        populateShowDetails(showDescription, showImage)
        binding.toolbar.title = showName
    }

    private fun populateShowDetails(description: String?, imageResourceId: Int) {
        binding.showDescriptionTextView.text = description
        binding.showImageView.setImageResource(imageResourceId)
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(requireContext())
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
            val intent = Intent(requireContext(), ShowDetailsFragment::class.java)
            intent.putExtra("reviewId", review.id)
            intent.putExtra("reviewRating", review.rating)
            intent.putExtra("reviewName", review.name)
            intent.putExtra("reviewComment", review.comment)
            intent.putExtra("reviewImage", review.imageResourceId)
            startActivity(intent)
        }
        binding.reviewsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.reviewsRecycler.adapter = adapter
        binding.reviewsRecycler.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        binding.reviewsRecycler.adapter = ReviewsAdapter(reviews) { review ->

            findNavController().navigate(R.id.reviewsRecycler)
        }

        val (totalRatings, averageRating) = calculateAverageRating(reviews)

        binding.ratingTextView.text = "%d ratings, %.2f average".format(totalRatings, averageRating)
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

        val averageRating = reviews.sumOf { it.rating }.toFloat() / reviews.size
        return Pair(reviews.size, averageRating)
    }

    private fun initAddReview() {
        binding.reviewButton.setOnClickListener {
            addNewReviewToList(Review(7, 5, "username", "comment", R.drawable.ic_profile_placeholder))
        }
    }

    private fun addNewReviewToList(item: Review) {
        adapter.addItem(item)
    }
}
