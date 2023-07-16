package infinuma.android.shows.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import infinuma.android.shows.R
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
