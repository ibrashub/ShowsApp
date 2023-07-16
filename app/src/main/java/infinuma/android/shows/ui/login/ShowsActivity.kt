package infinuma.android.shows.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import infinuma.android.shows.data.model.shows
import infinuma.android.shows.databinding.ActivityShowsBinding

class ShowsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowsBinding
    private lateinit var adapter: ShowsAdapter
    private lateinit var emptyStateTextView: TextView
    private lateinit var emptyStateIcon: ImageView
    private lateinit var showEmptyStateButton: FloatingActionButton
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initShowsRecycler()

        emptyStateTextView = binding.noShowsTextView
        showEmptyStateButton = binding.emptyStateButton
        emptyStateIcon = binding.noShowsIcon
        recyclerView = binding.showsRecycler

        showEmptyStateButton.setOnClickListener {
            if (recyclerView.visibility == View.VISIBLE) {
                recyclerView.visibility = View.GONE
                emptyStateTextView.visibility = View.VISIBLE
                emptyStateIcon.visibility = View.VISIBLE

            } else {
                recyclerView.visibility = View.VISIBLE
                emptyStateTextView.visibility = View.GONE
                emptyStateIcon.visibility = View.GONE
            }
        }


    }

    private fun initShowsRecycler() {
        adapter = ShowsAdapter(shows) { show ->
            Toast.makeText(this, show.name, Toast.LENGTH_SHORT).show()
        }
        binding.showsRecycler.layoutManager = LinearLayoutManager(this)

        binding.showsRecycler.adapter = adapter

        binding.showsRecycler.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

    }


}
