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

//Scroll down for activity lifecycle logs observed from Logcat.

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.loginButton.setOnClickListener {

            val intent = Intent(this, WelcomeActivity::class.java)
            val email = emailEditText.text.toString()

            val username = email.substringBefore("@")
            intent.putExtra("EMAIL_EXTRA", emailEditText.text.toString())
            intent.putExtra("username", username)
            startActivity(intent)
        }


        emailEditText = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.emailEditText)
        passwordEditText = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.passwordEditText)
        updateLoginButtonState()

        emailEditText.addTextChangedListener(object : TextWatcher {
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

        passwordEditText.addTextChangedListener(object : TextWatcher {
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

        Log.d("LoginActivity", "onCreate called")
    }

    private fun validateEmail(email: String) {
        // Implement your email validation logic using regex
        val regexPattern = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}"

        val isValidEmail = email.matches(Regex(regexPattern))

        if (isValidEmail) {
            // Email is valid, update UI if needed
            emailEditText.error = null
        } else {
            // Email is invalid, show error message
            emailEditText.error = "Invalid email"
        }

        // Update the Login button state based on email and password validity
        updateLoginButtonState()
    }

    private fun validatePassword(password: String) {
        val isValidPassword = password.length >= 6

        if (isValidPassword) {
            // Password is valid, update UI if needed
            passwordEditText.error = null
        } else {
            // Password is invalid, show error message
            passwordEditText.error = "Invalid password"
        }

        // Update the Login button state based on email and password validity
        updateLoginButtonState()
    }

    private fun updateLoginButtonState() {
        val isValidEmail = emailEditText.text?.isNotBlank() ?: false
        val isValidPassword = (passwordEditText.text?.length ?: 0) >= 6
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

    override fun onStart() {
        super.onStart()
        Log.d("LoginActivity", "onStart called")
    }
    override fun onResume() {
        super.onResume()
        Log.d("LoginActivity", "onResume called")
    }
    override fun onPause() {
        super.onPause()
        Log.w("LoginActivity", "onPause called")
    }
    override fun onStop() {
        super.onStop()
        Log.w("LoginActivity", "onStop called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.w("LoginActivity", "onDestroy called")
    }
    override fun onRestart() {
        super.onRestart()
        Log.i("LoginActivity", "onRestart called")
    }
}
/*
Logcat logs:
* 1. When the app is put on background and moved back to foreground:
onPause
onStop
onRestart
onStart
onResume    are called.
----------------
* 2. When the app is killed:
onPause
onStop
onDestroy   are called.
----------------
* 3. When the phone screen is locked and unlocked:
onPause
onStop
onRestart
onStart
onResume    are called, similar to when it's put on background and moved back to foreground.
----------------
* When the app is first opened:
onCreate
onStart
onResume    are called.
----------------
* When the app is reopened from the background:
onRestart
onStart
onResume    are called.
----------------
* When the app is put on background:
onPause
onStop      are called.
----------------
* When the app is closed/killed from the background:
onDestroy   is called.
*/