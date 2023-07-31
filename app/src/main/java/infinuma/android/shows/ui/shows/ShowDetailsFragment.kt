package infinuma.android.shows.ui.shows

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
import infinuma.android.shows.ui.login.ReviewsAdapter

class ShowDetailsFragment : Fragment() {

    private var _binding: FragmentShowDetailsBinding? = null
    private lateinit var adapter: ReviewsAdapter

    //    private val args by navArgs<ShowDetailsFragmentArgs>()
    private val viewModel by viewModels<ShowDetailsViewModel>()

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

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.reviewButton.setOnClickListener {
            showBottomSheetDialog()
        }

        val showName = arguments?.getString("showName")
        val showDescription = arguments?.getString("showDescription") ?: ""
        val showImage = arguments?.getString("showImage") ?: 0
        binding.toolbar.title = showName

        adapter = ReviewsAdapter(emptyList()) {}
        binding.reviewsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.reviewsRecycler.adapter = adapter
        binding.reviewsRecycler.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        val showId = arguments?.getString("showId") ?: ""
        viewModel.fetchReviews(showId)

        viewModel.reviewsLiveData.observe(viewLifecycleOwner) { reviews ->
            adapter.updateReviews(reviews)
        }

        viewModel.ratingTextViewLiveData.observe(viewLifecycleOwner) { (totalRatings, averageRating) ->
            binding.ratingTextView.text = getString(R.string.ratings_average, totalRatings, averageRating)
            binding.averageRatingBar.rating = averageRating
        }


        viewModel.descriptionLiveData.observe(viewLifecycleOwner) { description ->
            binding.showDescriptionTextView.text = description
        }

        viewModel.imageUrlLiveData.observe(viewLifecycleOwner) { imageUrl ->
            Glide.with(requireContext())
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.image_error)
                .into(binding.showImageView)
        }


        viewModel.populateShowData(showDescription, showImage as String)
        viewModel.calculateAverageRating()
        viewModel.reviewsLiveData.observe(viewLifecycleOwner) { updatedReviews ->
            adapter.updateReviews(updatedReviews)
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
            val rating = binding.ratingBar.rating.toInt()
            val comment = binding.commentEditText.text.toString()
            val userId = ""
            val userEmail = ""
            val userImageUrl = ""

            if (rating > 0) {
                viewModel.addNewReviewToList(rating, comment, userId, userEmail, userImageUrl)
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), R.string.provide_rating_error, Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }
}