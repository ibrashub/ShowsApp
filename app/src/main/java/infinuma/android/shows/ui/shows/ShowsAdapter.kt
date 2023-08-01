package infinuma.android.shows.ui.shows

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import infinuma.android.shows.R
import infinuma.android.shows.data.model.Show
import infinuma.android.shows.databinding.ItemShowBinding

class ShowsAdapter(
    private var items: List<Show>,
    private val onItemClickCallback: (Show) -> Unit
) : RecyclerView.Adapter<ShowsAdapter.ShowsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowsViewHolder {
        val binding = ItemShowBinding.inflate(LayoutInflater.from(parent.context))
        return ShowsViewHolder(binding)
    }

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: ShowsViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updateData(newShowsList: List<Show>) {
        items = newShowsList
        notifyDataSetChanged()
    }

    inner class ShowsViewHolder(private val binding: ItemShowBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val show = items[position]
                    onItemClickCallback(show)
                }
            }
        }

        fun bind(item: Show) {
            binding.showId.text = item.id.toString()
            binding.showName.text = item.title
            Glide.with(binding.showImage)
                .load(item.imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.image_error)
                .into(binding.showImage)
            binding.showDescription.text = item.description
            binding.root.setOnClickListener {
                onItemClickCallback(item)
            }
        }
    }

}