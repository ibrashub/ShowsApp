package infinuma.android.shows.ui.login

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import infinuma.android.shows.data.model.Review
import infinuma.android.shows.databinding.ItemReviewBinding

class ReviewsAdapter(
    private var reviewsList: List<Review>,
    private val onItemClickCallback: (Review) -> Unit
) : RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun getItemCount(): Int = reviewsList.count()

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviewsList[position])
    }

    fun updateReviews(updatedReviews: List<Review>) {
        reviewsList = updatedReviews
        notifyDataSetChanged()
    }

    inner class ReviewViewHolder(private val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Review) {
            binding.username.text = item.name
            binding.userImage.setImageResource(item.imageResourceId)
            binding.userComment.text = item.comment
            binding.starAmount.text = item.rating.toString()

        }
    }
}