package infinuma.android.shows.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import infinuma.android.shows.R
import infinuma.android.shows.data.model.Review
import infinuma.android.shows.data.model.reviews
import infinuma.android.shows.databinding.DialogAddReviewBinding
import infinuma.android.shows.databinding.FragmentShowDetailsBinding

class ShowDetailsFragment : Fragment() {

    private var _binding: FragmentShowDetailsBinding? = null
    private lateinit var adapter: ReviewsAdapter

    private val args by navArgs<ShowDetailsFragmentArgs>()

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

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        initReviewsRecycler()

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.showsFragment)
        }


        binding.reviewButton.setOnClickListener {
            showBottomSheetDialog()
        }

        val showName = arguments?.getString("showName")
        val showDescription = arguments?.getString("showDescription")
        val showImage = arguments?.getInt("showImage") ?: 0
        binding.toolbar.title = showName

        populateShowDetails(showDescription, showImage)
    }

    private fun populateShowDetails(description: String?, imageResourceId: Int) {
        binding.showDescriptionTextView.text = description
        binding.showImageView.setImageResource(imageResourceId)
    }

    private fun showBottomSheetDialog() {
        val dialog = BottomSheetDialog(requireContext())
        val binding = DialogAddReviewBinding.inflate(layoutInflater)

        dialog.setContentView(binding.root)

        binding.cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        binding.submitButton.setOnClickListener {
            val rating = binding.ratingBar.rating.toInt()
            val comment = binding.commentEditText.text.toString()

            if (rating > 0) {
                addNewReviewToList(rating, comment)

            } else {
                Toast.makeText(requireContext(), "Please provide a rating.", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun initReviewsRecycler() {
        adapter = ReviewsAdapter(reviews) { review ->
            val intent = Intent(requireContext(), initReviewsRecycler()::class.java)
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

    private fun addNewReviewToList(rating: Int, comment: String) {
        val review = Review(reviews.size + 1, rating, "username", comment, R.drawable.ic_profile_placeholder)

        adapter.addItem(review)
        adapter.notifyDataSetChanged()
        val (totalRatings, averageRating) = calculateAverageRating(reviews)
        binding.ratingTextView.text = "%d ratings, %.2f average".format(totalRatings, averageRating)
        binding.averageRatingBar.rating = averageRating
    }

}
