package infinuma.android.shows.ui.shows

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import infinuma.android.shows.R
import infinuma.android.shows.databinding.DialogAddReviewBinding
import infinuma.android.shows.databinding.FragmentShowDetailsBinding
import infinuma.android.shows.ui.login.REMEMBER_ME
import infinuma.android.shows.ui.login.ReviewsAdapter

class ShowDetailsFragment : Fragment() {

    private var _binding: FragmentShowDetailsBinding? = null
    private lateinit var adapter: ReviewsAdapter
    private val viewModel by viewModels<ShowDetailsViewModel>()
    private lateinit var sharedPreferences3: SharedPreferences

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
        sharedPreferences3 = requireContext().getSharedPreferences(REMEMBER_ME, Context.MODE_PRIVATE)


        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.reviewButton.setOnClickListener {
            showBottomSheetDialog()
        }

        val showId = arguments?.getInt("showId") ?: 0
        val showName = arguments?.getString("showName")
        binding.toolbar.title = showName

        viewModel.fetchShowDetails(showId)

        viewModel.fetchReviews(showId)



        adapter = ReviewsAdapter(emptyList()) {}
        binding.reviewsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.reviewsRecycler.adapter = adapter
        binding.reviewsRecycler.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        viewModel.showDetailsLiveData.observe(viewLifecycleOwner) { showDetails ->
            //binding.showName.text = showDetails.title
            Glide.with(requireContext())
                .load(showDetails.imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.image_error)
                .into(binding.showImageView)
            binding.showDescriptionTextView.text = showDetails.description
        }



        viewModel.reviewsLiveData.observe(viewLifecycleOwner) { reviews ->
            adapter.updateReviews(reviews)
        }

        viewModel.ratingTextViewLiveData.observe(viewLifecycleOwner) { (totalRatings, averageRating) ->
            binding.ratingTextView.text = getString(R.string.ratings_average, totalRatings, averageRating)
            binding.averageRatingBar.rating = averageRating
        }

        viewModel.reviewsLiveData.observe(viewLifecycleOwner) { reviews ->
            if (reviews == null || reviews.isEmpty()) {
                binding.noReviewsTextView.visibility = View.VISIBLE
                binding.reviewsRecycler.visibility = View.GONE
                binding.ratingTextView.visibility = View.GONE
                binding.averageRatingBar.visibility = View.GONE
            } else {
                binding.noReviewsTextView.visibility = View.GONE
                binding.reviewsRecycler.visibility = View.VISIBLE
                binding.ratingTextView.visibility = View.VISIBLE
                binding.averageRatingBar.visibility = View.VISIBLE

                adapter.updateReviews(reviews)
            }
        }
    }

    private fun showBottomSheetDialog() {
        val dialog = BottomSheetDialog(requireContext())
        val binding = DialogAddReviewBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        binding.cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        binding.submitButton.setOnClickListener {
            val id = ""
            val comment = binding.commentEditText.text.toString()
            val rating = binding.ratingBar.rating.toInt()
            val showId = 0


            if (rating > 0) {
                viewModel.addNewReviewToList(comment = comment, rating = rating,  showId = showId)
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), R.string.provide_rating_error, Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }
}