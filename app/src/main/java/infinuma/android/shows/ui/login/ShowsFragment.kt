package infinuma.android.shows.ui.login

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import infinuma.android.shows.R
import infinuma.android.shows.data.model.ShowsViewModel
import infinuma.android.shows.databinding.DialogProfileSettingsBinding
import infinuma.android.shows.databinding.FragmentShowsBinding

class ShowsFragment : Fragment(R.layout.fragment_shows) {

    private var _binding: FragmentShowsBinding? = null
    private lateinit var adapter: ShowsAdapter
    private val viewModel by viewModels<ShowsViewModel>()
    private lateinit var sharedPreferences: SharedPreferences

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("user_email", Context.MODE_PRIVATE)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentShowsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.showsProfilePhoto.setOnClickListener {
            showProfileBottomSheetDialog()
        }


        viewModel.showsLiveData.observe(viewLifecycleOwner) { showsList ->
            adapter = ShowsAdapter(showsList) { show ->
                val destination = ShowsFragmentDirections.actionShowsFragmentToShowDetailsFragment(
                    showId = show.id,
                    showName = show.name,
                    showDescription = show.description,
                    showImage = show.imageResourceId
                )
                findNavController().navigate(destination)
            }
            binding.showsRecycler.layoutManager = LinearLayoutManager(requireContext())
            binding.showsRecycler.adapter = adapter
            binding.showsRecycler.addItemDecoration(
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            )

        }

        binding.emptyStateButton.setOnClickListener {
            if (binding.showsRecycler.visibility == View.VISIBLE) {
                binding.showsRecycler.visibility = View.GONE
                binding.noShowsTextView.visibility = View.VISIBLE
                binding.noShowsIcon.visibility = View.VISIBLE
            } else {
                binding.showsRecycler.visibility = View.VISIBLE
                binding.noShowsTextView.visibility = View.GONE
                binding.noShowsIcon.visibility = View.GONE
            }
        }
    }

    private fun showProfileBottomSheetDialog() {
        val dialog = BottomSheetDialog(requireContext())
        val binding = DialogProfileSettingsBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        val sharedPreferences = requireContext().getSharedPreferences("user_email", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("user_email", "default1@email.com") ?: "user_email"
        val userProfilePhoto = R.drawable.ic_profile_placeholder

        binding.emailTextProfile.text = userEmail
        binding.profilePhoto.setImageResource(userProfilePhoto)

        binding.changePhotoButton.setOnClickListener {
            dialog.dismiss()
        }

        binding.LogoutButton.setOnClickListener {
            sharedPreferences.edit {
                putBoolean("rememberMeCheckbox", false)
                remove("user_email")
                dialog.dismiss()
            }

            requireActivity().supportFragmentManager.popBackStack(
                R.id.loginFragment,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )

            val builder = AlertDialog.Builder(context)
            builder
                .setTitle("Logout confirmation")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton(R.string.yes) { dialog, _ ->
                    findNavController().navigate(R.id.loginFragment)
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.no) { dialog, _ ->
                    dialog.dismiss()
                }

            builder.show()

        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

