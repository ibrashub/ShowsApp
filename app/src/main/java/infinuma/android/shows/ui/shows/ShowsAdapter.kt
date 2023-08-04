package infinuma.android.shows.ui.shows

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import infinuma.android.shows.R
import infinuma.android.shows.data.model.db_shows.ShowDB
import infinuma.android.shows.networking.responses.Show
import infinuma.android.shows.databinding.ItemShowBinding

class ShowsAdapter(
    private var items: List<ShowDB>,
    private val onItemClickCallback: (ShowDB) -> Unit
) : RecyclerView.Adapter<ShowsAdapter.ShowsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowsViewHolder {
        val binding = ItemShowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShowsViewHolder( binding)
    }

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: ShowsViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updateData(newShowsList: List<ShowDB>) {
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

        fun bind(item: ShowDB) {
            //binding.showId.text = item.id.toString()
            //binding.showName.text = item.title
            //Glide.with(binding.showImage)
                //.load(item.imageUrl)
                //.placeholder(R.drawable.placeholder_image)
                //.error(R.drawable.image_error)
                //.into(binding.showImage)
            binding.showName.text = item.name
            binding.showDescription.text = item.description
            binding.showImage.setImageResource(item.image)
            binding.root.setOnClickListener {
                onItemClickCallback(item)
            }
        }
    }

}