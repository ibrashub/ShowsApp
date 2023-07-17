package infinuma.android.shows

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import infinuma.android.shows.data.model.Show
import infinuma.android.shows.data.model.shows
import infinuma.android.shows.databinding.ActivityShowDetailsBinding
import infinuma.android.shows.ui.login.ShowsActivity

class ShowDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowDetailsBinding
    private lateinit var showNameTextView: TextView
    private lateinit var reviewButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        reviewButton = binding.reviewButton

        reviewButton.setOnClickListener {
            showAlertDialog()
        }

        val showId = intent.getStringExtra("showId")
        val showName = intent.getStringExtra("showName")
        val showDescription = intent.getStringExtra("showDescription")
        val showImage = intent.getIntExtra("showImage", 0)

        populateShowDetails(showName, showDescription, showImage)
        supportActionBar?.title = showName
    }

    private fun populateShowDetails(name: String?, description: String?, imageResourceId: Int) {
        binding.showDescriptionTextView.text = description
        binding.showImageView.setImageResource(imageResourceId)
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder
            .setTitle((R.string.app_name))
            .setMessage("Are you sure you want to add a new review?")
            .setPositiveButton(R.string.add) { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }

        val alertDialog = builder.create()
        alertDialog.show()
    }


}


