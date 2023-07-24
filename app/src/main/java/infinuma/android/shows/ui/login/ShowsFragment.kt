package infinuma.android.shows.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import infinuma.android.shows.R
import infinuma.android.shows.data.model.ShowsViewModel
import infinuma.android.shows.databinding.FragmentShowsBinding

class ShowsFragment : Fragment(R.layout.fragment_shows) {

    private var _binding: FragmentShowsBinding? = null
    private lateinit var adapter: ShowsAdapter
    private val viewModel by viewModels<ShowsViewModel>()

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentShowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

            binding.logoutButton.setOnClickListener {
                findNavController().navigate(R.id.loginFragment)
                requireActivity().supportFragmentManager.popBackStack(
                    R.id.loginFragment,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
            }

        }


        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }


    }

