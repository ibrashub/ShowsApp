package infinuma.android.shows.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import infinuma.android.shows.ShowDetailsActivity
import infinuma.android.shows.data.model.shows
import infinuma.android.shows.databinding.ActivityShowsBinding

class ShowsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowsBinding
    private lateinit var adapter: ShowsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initShowsRecycler()


        binding.emptyStateButton.setOnClickListener {
            if (binding.showsRecycler.visibility == View.VISIBLE) {
                binding.showsRecycler.visibility = View.GONE
                binding.noShowsTextView.visibility = View.VISIBLE
                binding.noShowsIcon.visibility = View.VISIBLE

            } else {
                binding.showsRecycler.visibility = View.VISIBLE
                binding.noShowsTextView.visibility = View.GONE
                binding.noShowsIcon.visibility = View.GONE
            }
        }


    }

    private fun initShowsRecycler() {
        adapter = ShowsAdapter(shows) { show ->
            val intent = Intent(this, ShowDetailsActivity::class.java)
            intent.putExtra("showId", show.id)
            intent.putExtra("showName", show.name)
            intent.putExtra("showDescription", show.description)
            intent.putExtra("showImage", show.imageResourceId)
            startActivity(intent)
        }
        binding.showsRecycler.layoutManager = LinearLayoutManager(this)

        binding.showsRecycler.adapter = adapter

        binding.showsRecycler.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

    }


}
