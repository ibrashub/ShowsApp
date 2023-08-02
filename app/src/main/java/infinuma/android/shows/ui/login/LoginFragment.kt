package infinuma.android.shows.ui.login

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import infinuma.android.shows.R
import infinuma.android.shows.databinding.FragmentLoginBinding
import infinuma.android.shows.networking.ApiModule

const val REMEMBER_ME = "remember_me_data"
const val USER_EMAIL = "user_email"
const val PREFERENCE_SHOW = "show"
private const val ACCESS_TOKEN = "access-token"
private const val CLIENT = "client"
private const val UID = "uid"

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferences2: SharedPreferences
    private lateinit var sharedPreferences3: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ApiModule.initRetrofit(requireContext())

        sharedPreferences = requireContext().getSharedPreferences(USER_EMAIL, Context.MODE_PRIVATE)
        sharedPreferences2 = requireContext().getSharedPreferences(PREFERENCE_SHOW, Context.MODE_PRIVATE)
        sharedPreferences3 = requireContext().getSharedPreferences(REMEMBER_ME, Context.MODE_PRIVATE)

        viewModel.loginLiveData.observe(this) { isSuccessful ->
            if (isSuccessful) {
                val accessToken = viewModel.getAccessToken()
                val client = viewModel.getClient()
                val uid = viewModel.getUid()

                ApiModule.setAuthHeaders(accessToken, client, uid)
                sharedPreferences3.edit {
                    putString(ACCESS_TOKEN, accessToken)
                    putString(CLIENT, client)
                    putString(UID, uid)
                }

                findNavController().navigate(R.id.action_loginFragment_to_showsFragment)

            } else {
                Toast.makeText(requireContext(), R.string.invalid_login, Toast.LENGTH_SHORT).show()
            }
        }


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLoginButton()

        val rememberMeCheckbox = sharedPreferences3.getBoolean(REMEMBER_ME, false)
        if (rememberMeCheckbox) {
            val accessToken = sharedPreferences3.getString(ACCESS_TOKEN, null)
            val client = sharedPreferences3.getString(CLIENT, null)
            val uid = sharedPreferences3.getString(UID, null)

            if (accessToken != null && client != null && uid != null) {
                ApiModule.setAuthHeaders(accessToken, client, uid)
                findNavController().navigate(R.id.action_loginFragment_to_showsFragment)
            } else {
                Toast.makeText(requireContext(), R.string.auth_error, Toast.LENGTH_SHORT).show()
            }
        }

        val registrationSuccess = arguments?.getBoolean("registrationSuccess", false) ?: false
        if (registrationSuccess) {
            binding.loginText.text = getString(R.string.registration_successful)
            binding.registerButton.visibility = View.INVISIBLE
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

    private fun initLoginButton() = with(binding) {
        loginButton.setOnClickListener {
            viewModel.onLoginButtonClicked(
                username = emailEditText.text.toString(),
                password = passwordEditText.text.toString()
            )
            val email = binding.emailEditText.text.toString()
            sharedPreferences.edit {
                putString(USER_EMAIL, email)
            }
        }
    }

    private fun initListeners() {
        binding.registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.rememberMeCheckbox.setOnCheckedChangeListener { _, _ ->
            sharedPreferences3.edit {
                putBoolean(REMEMBER_ME, binding.rememberMeCheckbox.isChecked)
            }
        }
    }

    private fun validateEmail(email: String) {
        val regexPattern = getString(R.string.regex_pattern)

        val isValidEmail = email.matches(Regex(regexPattern))

        binding.emailEditText.error = if (isValidEmail) null else getString(R.string.invalid_email)
        updateLoginButtonState()
    }

    private fun validatePassword(password: String) {
        val isValidPassword = password.length >= 6

        binding.passwordEditText.error = if (isValidPassword) null else getString(R.string.invalid_password)
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




