package infinuma.android.shows.ui.login

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
            binding.showName.text = item.name
            binding.showImage.setImageResource(item.imageResourceId)
            binding.showDescription.text = item.description
            binding.root.setOnClickListener {
                onItemClickCallback(item)
            }
        }
    }

}