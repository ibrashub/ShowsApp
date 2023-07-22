package infinuma.android.shows.navigation_component

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import infinuma.android.shows.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}