package infinuma.android.shows

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        val intent = intent
        val email = intent.getStringExtra("EMAIL_EXTRA")
        val username = extractUsernameFromEmail(email ?: "")
        val welcomeTextView = findViewById<TextView>(R.id.welcomeText)
        welcomeTextView.text = getString(R.string.usernameWelcome, username)


    }

    private fun extractUsernameFromEmail(email: String): String {
        val atIndex = email.indexOf('@')
        if (atIndex != -1) {
            return email.substring(0, atIndex)
        }
        return email
    }

}