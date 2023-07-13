package infinuma.android.shows

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import infinuma.android.shows.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Instantiate the binding object
        val binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the email from the intent
        val email = intent.getStringExtra("email_full_form")

        // Extract the username from the email
        val username = email?.substringBefore("@")

        // Access the views using the binding object
        binding.welcomeText.text = getString(R.string.usernameWelcome, username)
    }


}