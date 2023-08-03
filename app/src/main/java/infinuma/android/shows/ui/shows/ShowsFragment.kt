package infinuma.android.shows.ui.shows

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import infinuma.android.shows.FileUtil
import infinuma.android.shows.R
import infinuma.android.shows.networking.responses.Show
import infinuma.android.shows.networking.responses.User
import infinuma.android.shows.databinding.DialogProfileSettingsBinding
import infinuma.android.shows.databinding.FragmentShowsBinding
import infinuma.android.shows.networking.FileUploadService
import infinuma.android.shows.ui.login.PREFERENCE_SHOW
import infinuma.android.shows.ui.login.REMEMBER_ME
import infinuma.android.shows.ui.login.USER_EMAIL
import java.io.File
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://tv-shows.infinum.academy/"

class ShowsFragment : Fragment(R.layout.fragment_shows) {

    private var _binding: FragmentShowsBinding? = null
    private lateinit var adapter: ShowsAdapter
    private val viewModel by viewModels<ShowsViewModel>()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var user: User

    private val binding get() = _binding!!

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences(USER_EMAIL, Context.MODE_PRIVATE)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentShowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(this, ShowsViewModelFactory(sharedPreferences))[ShowsViewModel::class.java]
        val email = ""
        val userId = ""
        val accessToken = ""
        val client = ""
        val imageUrl = ""
        user = User(userId, email = email, accessToken = accessToken, client = client, imageUrl = imageUrl)
        viewModel.setCurrentUser(user = user)
        binding.showsProfilePhoto.setOnClickListener {
            showProfileBottomSheetDialog()
        }

        setProfileImage()
        initRecyclerView()
        observeShowsLiveData()

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
        val imageUriString = sharedPreferences.getString(PREFERENCE_SHOW, null)
        val userEmail =
            sharedPreferences.getString(USER_EMAIL, R.string.default_email.toString()) ?: USER_EMAIL

        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            binding.profilePhoto.setImageURI(imageUri)
        }

        binding.emailTextProfile.text = userEmail

        binding.changePhotoButton.setOnClickListener {
            dialog.dismiss()
            val builder = AlertDialog.Builder(context)
            builder
                .setTitle(R.string.change_photo_dialog_title)
                .setMessage(R.string.change_photo_dialog_body)
                .setPositiveButton(R.string.camera) { dialog, _ ->
                    takeImage()
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.gallery) { dialog, _ ->
                    selectImageFromGallery()
                    dialog.dismiss()
                }

            builder.show()
        }


        binding.LogoutButton.setOnClickListener {
            dialog.dismiss()
            val builder = AlertDialog.Builder(context)
            builder
                .setTitle(R.string.logout_confirmation_title)
                .setMessage(R.string.logout_confirmation_message)
                .setPositiveButton(R.string.yes) { dialog, _ ->
                    sharedPreferences.edit {
                        remove(USER_EMAIL)
                    }
                    sharedPreferences.edit {
                        remove(PREFERENCE_SHOW)
                    }
                    sharedPreferences.edit {
                        remove(REMEMBER_ME)
                    }
                    findNavController().navigate(R.id.action_showsFragment_to_loginFragment)
                    dialog.dismiss()

                }
                .setNegativeButton(R.string.no) { dialog, _ ->
                    dialog.dismiss()
                }
            builder.show()
        }
        dialog.show()
    }

    private val selectImageFromGalleryResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val resizedImageFile = FileUtil.resizeAndSaveImage(requireContext(), uri)
            binding.showsProfilePhoto.setImageURI(uri)
            viewModel.onProfilePhotoSelected(uri)
            sharedPreferences.edit {
                putString(PREFERENCE_SHOW, resizedImageFile?.absolutePath)
            }
            val photoFile = File(resizedImageFile?.path)
            val requestFile = photoFile.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", photoFile.name, requestFile)

            uploadPhotoToServer(body)
        }
    }

    private var latestTmpUri: Uri? = null

    private val takeImageResult = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            latestTmpUri?.let { uri ->
                val resizedImageFile = FileUtil.resizeAndSaveImage(requireContext(), uri)
                binding.showsProfilePhoto.setImageURI(uri)
                viewModel.onProfilePhotoSelected(uri)
                sharedPreferences.edit {
                    putString(PREFERENCE_SHOW, resizedImageFile?.absolutePath)
                }
                val photoFile = File(resizedImageFile?.path)
                val requestFile = photoFile.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("file", photoFile.name, requestFile)

                uploadPhotoToServer(body)
            }
        }
    }

    private fun uploadPhotoToServer(body: MultipartBody.Part) {
        val fileUploadService = retrofit.create(FileUploadService::class.java)
        lifecycleScope.launchWhenStarted {
            try {
                val response = fileUploadService.uploadPhoto(body)
                if (response.isSuccessful) {
                    val photoUploadResponse = response.body()
                    val photoUrl = photoUploadResponse?.user?.imageUrl
                    //sharedPreferences.
                } else {
                    Log.e("File Upload", "Upload failed. Response code: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("File Upload", "Error uploading file: ${e.message}")
            }
        }
    }

    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    private fun getTmpFileUri(): Uri {
        val tmpFileName = "tmp_image_file.png"
        val tmpFile = File(requireContext().cacheDir, tmpFileName).apply {
            createNewFile()
            deleteOnExit()
        }
        return FileProvider.getUriForFile(
            requireContext().applicationContext,
            "${requireContext().packageName}.provider",
            tmpFile
        )
    }

    private fun setProfileImage() {
        val imageFilePath = sharedPreferences.getString(PREFERENCE_SHOW, null)
        if (imageFilePath != null) {
            val imageUri = Uri.fromFile(File(imageFilePath))
            binding.showsProfilePhoto.setImageURI(imageUri)
        }
    }

    private fun initRecyclerView() {
        binding.showsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.showsRecycler.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )
        adapter = ShowsAdapter(emptyList()) { show ->
            val destination = ShowsFragmentDirections.actionShowsFragmentToShowDetailsFragment(
                showId = show.id,
                showName = show.title
            )
            findNavController().navigate(destination)
        }
        binding.showsRecycler.adapter = adapter

    }

    private fun setupAdapter(showsList: List<Show>) {
        adapter.updateData(showsList)
    }

    private fun observeShowsLiveData() {
        viewModel.showsLiveData.observe(viewLifecycleOwner) { showsList ->
            Log.d("ShowsFragment", "Received showsLiveData with data: $showsList")
            setupAdapter(showsList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class ShowsViewModelFactory(private val sharedPreferences: SharedPreferences) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowsViewModel::class.java)) {
            return ShowsViewModel(sharedPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}