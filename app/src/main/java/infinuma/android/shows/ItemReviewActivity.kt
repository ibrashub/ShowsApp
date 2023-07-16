package infinuma.android.shows

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import infinuma.android.shows.databinding.ItemReviewBinding

class ItemReviewActivity : AppCompatActivity() {

    private lateinit var binding: ItemReviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_review)
    }
}