package infinuma.android.shows

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import infinuma.android.shows.databinding.ActivityLoginBinding
import infinuma.android.shows.ui.login.ShowsActivity

//Scroll down for activity lifecycle logs observed from Logcat.

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {

            val intent = Intent(this, ShowsActivity::class.java)
            startActivity(intent)
        }

        updateLoginButtonState()

        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed before text changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Perform validation after text changed
                validateEmail(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // No action needed after text changed
            }
        })

        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed before text changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Perform validation after text changed
                validatePassword(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // No action needed after text changed
            }
        })

    }

    private fun validateEmail(email: String) {
        // Implement your email validation logic using regex
        val regexPattern = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}"

        val isValidEmail = email.matches(Regex(regexPattern))

        binding.emailEditText.error = if (isValidEmail) null else "Invalid Email"
        updateLoginButtonState()
    }

    private fun validatePassword(password: String) {
        val isValidPassword = password.length >= 6

        binding.passwordEditText.error = if (isValidPassword) null else "Invalid password"
        updateLoginButtonState()
    }

    private fun updateLoginButtonState() {
        val isValidEmail = binding.emailEditText.text?.isNotBlank() ?: false
        val isValidPassword = (binding.passwordEditText.text?.length ?: 0) >= 6
        val loginButton = findViewById<Button>(R.id.loginButton)


        if (isValidEmail && isValidPassword) {
            loginButton.isEnabled = true
            loginButton.backgroundTintList = ContextCompat.getColorStateList(this, R.color.white)
            loginButton.setTextColor(ContextCompat.getColor(this, R.color.purple_background))
        } else {
            loginButton.isEnabled = false
            loginButton.backgroundTintList = ContextCompat.getColorStateList(this, R.color.disabled_button_color)
            loginButton.setTextColor(ContextCompat.getColor(this, R.color.white))
        }
    }
}