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

const val REMEMBER_ME = "rememberMeCheckbox"
const val USER_EMAIL = "user_email"
const val PREFERENCE_SHOW = "show"

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferences2: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences(USER_EMAIL, Context.MODE_PRIVATE)
        sharedPreferences2 = requireContext().getSharedPreferences(PREFERENCE_SHOW, Context.MODE_PRIVATE)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rememberMeCheckbox = sharedPreferences.getBoolean(REMEMBER_ME, false)
        if (rememberMeCheckbox) {
            findNavController().navigate(R.id.action_loginFragment_to_showsFragment)
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
            val email = binding.emailEditText.text.toString()
            sharedPreferences.edit {
                putString(USER_EMAIL, email)
            }
            findNavController().navigate(R.id.action_loginFragment_to_showsFragment)
        }

        binding.rememberMeCheckbox.setOnCheckedChangeListener { rememberMeCheckbox, isChecked ->
            sharedPreferences.edit {
                putBoolean(REMEMBER_ME, binding.rememberMeCheckbox.isChecked)
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




