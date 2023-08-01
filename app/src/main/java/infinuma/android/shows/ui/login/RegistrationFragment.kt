package infinuma.android.shows.ui.login

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import infinuma.android.shows.R
import infinuma.android.shows.databinding.FragmentRegistrationBinding
import infinuma.android.shows.networking.ApiModule

class RegistrationFragment : Fragment(R.layout.fragment_registration) {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegistrationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ApiModule.initRetrofit(requireContext())

        viewModel.registrationResultLiveData.observe(this) { isSuccessful ->
            if (isSuccessful) {
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment, bundleOf("registrationSuccess" to true))
            } else {
                val builder = AlertDialog.Builder(context)
                builder
                    .setTitle("Registration failed!")
                    .setMessage("Email already exists.")
                    .setPositiveButton("Try again") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRegisterButton()


        updateRegisterButtonState()


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

        binding.repeatPasswordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateRepeatPassword(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun initRegisterButton() = with(binding) {
        registerButton.setOnClickListener {
            viewModel.onRegisterButtonClicked(
                username = emailEditText.text.toString(),
                password = passwordEditText.text.toString()
            )
        }
    }

    private fun validateEmail(email: String) {
        val regexPattern = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}"

        val isValidEmail = email.matches(Regex(regexPattern))

        binding.emailEditText.error = if (isValidEmail) null else "Invalid Email"
        updateRegisterButtonState()
    }

    private fun validatePassword(password: String) {
        val isValidPassword = password.length >= 6

        binding.passwordEditText.error = if (isValidPassword) null else "Invalid password"
        updateRegisterButtonState()
    }

    private fun validateRepeatPassword(password: String) {
        val isValidRepeatPassword = password == binding.passwordEditText.toString()

        binding.passwordEditText.error = if (isValidRepeatPassword) null else "Invalid password"
        updateRegisterButtonState()
    }

    private fun updateRegisterButtonState() {
        val isValidEmail = binding.emailEditText.text?.isNotBlank() ?: false
        val isValidPassword = (binding.passwordEditText.text?.length ?: 0) >= 6
        val isValidRepeatPassword = (binding.repeatPasswordEditText.text?.length ?: 0) >= 6
        val registerButton = binding.registerButton


        if (isValidEmail && isValidPassword && isValidRepeatPassword) {
            registerButton.isEnabled = true
            registerButton.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
            registerButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_background))
        } else {
            registerButton.isEnabled = false
            registerButton.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.disabled_button_color)
            registerButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}




