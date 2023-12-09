package infinuma.android.shows.ui.login

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import infinuma.android.shows.R
import infinuma.android.shows.networking.responses.Review
import infinuma.android.shows.databinding.ItemReviewBinding

class ReviewsAdapter(
    private var reviews: List<Review>,
    private val onItemClickCallback: (Review) -> Unit
) : RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun getItemCount(): Int = reviews.count()

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    fun updateReviews(newReviews: List<Review>) {
        reviews = newReviews
        notifyDataSetChanged()
    }

    inner class ReviewViewHolder(private val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {
            binding.username.text = review.user.email
            binding.userComment.text = review.comment
            binding.starAmount.text = review.rating.toString()
            Glide.with(binding.userImage)
                .load(review.user.imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.image_error)
                .into(binding.userImage)

        }
    }
}