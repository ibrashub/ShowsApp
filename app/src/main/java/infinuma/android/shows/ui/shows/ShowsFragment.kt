package infinuma.android.shows.ui.shows

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import infinuma.android.shows.FileUtil
import infinuma.android.shows.R
import infinuma.android.shows.data.model.ShowsViewModel
import infinuma.android.shows.databinding.DialogProfileSettingsBinding
import infinuma.android.shows.databinding.FragmentShowsBinding
import infinuma.android.shows.ui.login.REMEMBER_ME
import infinuma.android.shows.ui.login.USER_EMAIL
import java.io.File

class ShowsFragment : Fragment(R.layout.fragment_shows) {

    private var _binding: FragmentShowsBinding? = null
    private lateinit var adapter: ShowsAdapter
    private val viewModel by viewModels<ShowsViewModel>()
    private lateinit var sharedPreferences: SharedPreferences
    private val binding get() = _binding!!

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

        binding.showsProfilePhoto.setOnClickListener {
            showProfileBottomSheetDialog()
        }

        checkIfPermissionNeeded()

        val imageFilePath = sharedPreferences.getString("image_path", null)
        if (imageFilePath != null) {
            val imageUri = Uri.fromFile(File(imageFilePath))
            binding.showsProfilePhoto.setImageURI(imageUri)
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
        val imageUriString = sharedPreferences.getString("image_path", null)
        val userEmail =
            sharedPreferences.getString(USER_EMAIL, R.string.default_email.toString()) ?: USER_EMAIL

        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            binding.profilePhoto.setImageURI(imageUri)
        }

        binding.emailTextProfile.text = userEmail

        binding.changePhotoButton.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder
                .setTitle("Choose")
                .setMessage("Take a new photo")
                .setPositiveButton("Camera") { dialog, _ ->
                    takeImage()
                    dialog.dismiss()
                }
                .setNegativeButton("Gallery") { dialog, _ ->
                    selectImageFromGallery()
                    dialog.dismiss()
                }

            builder.show()
        }


        binding.LogoutButton.setOnClickListener {
            dialog.dismiss()
            findNavController().popBackStack(R.id.showsFragment, true)

            val builder = AlertDialog.Builder(context)
            builder
                .setTitle(R.string.logout_confirmation_title)
                .setMessage(R.string.logout_confirmation_message)
                .setPositiveButton(R.string.yes) { dialog, _ ->
                    sharedPreferences.edit {
                        remove(USER_EMAIL)
                        putBoolean(REMEMBER_ME, false)
                        remove("image_path")

                    }
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

    private fun checkIfPermissionNeeded() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is already available, run some code
        } else {
            // Permission is missing and must be requested.
            requestCameraPermission()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val grantedPermissions = permissions.filterValues { it }.keys
        if (grantedPermissions.contains(Manifest.permission.READ_MEDIA_IMAGES) &&
            grantedPermissions.contains(Manifest.permission.READ_MEDIA_VIDEO)) {
        } else {}
    }

    private fun requestCameraPermission() {
        val permissions = arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO
        )
        requestPermissionLauncher.launch(permissions)
    }

    private val selectImageFromGalleryResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val resizedImageFile = FileUtil.resizeAndSaveImage(requireContext(), uri)
            val bitmap = BitmapFactory.decodeFile(resizedImageFile?.path)
            binding.showsProfilePhoto.setImageBitmap(bitmap)

            val sharedPreferences = requireContext().getSharedPreferences(USER_EMAIL, Context.MODE_PRIVATE)
            sharedPreferences.edit {
                putString("image_path", resizedImageFile?.absolutePath)
            }
        }
    }

    private var latestTmpUri: Uri? = null

    private fun takeImage() {
        val tmpFileUri = getTmpFileUri()
        latestTmpUri = tmpFileUri // Set the latestTmpUri to the current tmpFileUri

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, tmpFileUri)
        takePictureResult.launch(takePictureIntent)

        Log.d("ShowsFragment", "LatestTmpUri: $latestTmpUri") // Add this line for debugging
    }

    private val takePictureResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri = latestTmpUri ?: run {
                Log.e("ShowsFragment", "LatestTmpUri is null.")
                return@registerForActivityResult
            }
            try {
                val imageFile = File(imageUri.path)
                if (imageFile.exists()) {
                    // Save the image file to a permanent location
                    val imageFileName = "profile_photo.jpg"
                    val destFile = File(requireContext().filesDir, imageFileName)
                    imageFile.copyTo(destFile, overwrite = true)

                    // Save the image file path in SharedPreferences
                    val sharedPreferences = requireContext().getSharedPreferences(USER_EMAIL, Context.MODE_PRIVATE)
                    sharedPreferences.edit {
                        putString("image_path", destFile.absolutePath)
                    }

                    // Update the ImageView with the new profile photo
                    val bitmap = BitmapFactory.decodeFile(destFile.absolutePath)
                    binding.showsProfilePhoto.setImageBitmap(bitmap)
                } else {
                    Log.e("ShowsFragment", "Image file does not exist at path: ${imageUri.path}")
                }
            } catch (e: Exception) {
                Log.e("ShowsFragment", "Error while copying image: ${e.message}")
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}