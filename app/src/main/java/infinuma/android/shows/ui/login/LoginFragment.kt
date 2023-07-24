package infinuma.android.shows.ui.login

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import infinuma.android.shows.R
import infinuma.android.shows.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("Remember Me", Context.MODE_PRIVATE)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rememberMeCheckbox = sharedPreferences.getBoolean("rememberMeCheckbox", false)
        if (rememberMeCheckbox) {
            findNavController().navigate(R.id.showsFragment)
        }

        updateLoginButtonState()

        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateEmail(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePassword(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

    }

    private fun initListeners() {
        binding.loginButton.setOnClickListener {
            findNavController().navigate(R.id.showsFragment)
        }

        binding.rememberMeCheckbox.setOnCheckedChangeListener { rememberMeCheckbox, isChecked ->
            sharedPreferences.edit {
                putBoolean("rememberMeCheckbox", binding.rememberMeCheckbox.isChecked)
            }
        }
    }

    private fun validateEmail(email: String) {
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
        val loginButton = binding.loginButton


        if (isValidEmail && isValidPassword) {
            loginButton.isEnabled = true
            loginButton.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
            loginButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_background))
        } else {
            loginButton.isEnabled = false
            loginButton.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.disabled_button_color)
            loginButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
        initListeners()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}




