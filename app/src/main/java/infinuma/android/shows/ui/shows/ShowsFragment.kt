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
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.button.MaterialButton
import infinuma.android.shows.FileUtil
import infinuma.android.shows.R
import infinuma.android.shows.data.model.ShowsViewModel
import infinuma.android.shows.databinding.DialogProfileSettingsBinding
import infinuma.android.shows.databinding.FragmentShowsBinding
import infinuma.android.shows.ui.login.REMEMBER_ME
import infinuma.android.shows.ui.login.USER_EMAIL
import java.io.File
import kotlinx.coroutines.launch

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

        val sharedPreferences by lazy {
            requireContext().getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        }
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
        val imageUriString = sharedPreferences.getString("image_uri", null)
        val userEmail =
            sharedPreferences.getString(USER_EMAIL, R.string.default_email.toString()) ?: USER_EMAIL
        //val userProfilePhoto = R.drawable.ic_profile_placeholder


        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            binding.profilePhoto.setImageURI(imageUri)
        }

        binding.emailTextProfile.text = userEmail
        //

        binding.changePhotoButton.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder
                .setTitle("Choose")
                .setMessage("Take a new photo")
                .setPositiveButton("Camera") { dialog, _ ->
                    //                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    //                    startActivity(cameraIntent)
                    ActivityResultContracts.TakePicture()

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
            grantedPermissions.contains(Manifest.permission.READ_MEDIA_VIDEO) &&
            grantedPermissions.contains(Manifest.permission.READ_MEDIA_AUDIO)) {
            // All necessary permissions have been granted. You can now access media files.
            // For example, you can call the selectImageFromGalleryResult.launch() method here.
        } else {
            // Some or all permissions were denied. Handle the situation accordingly.
            // You can show a message to the user explaining why the permissions are needed.
        }
    }

    private fun requestCameraPermission() {
        val permissions = arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_AUDIO
        )
        requestPermissionLauncher.launch(permissions)
    }

    private val selectImageFromGalleryResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val resizedImageFile = FileUtil.resizeAndSaveImage(requireContext(), uri)
            val bitmap = BitmapFactory.decodeFile(resizedImageFile?.path)
            binding.showsProfilePhoto.setImageBitmap(bitmap)

            val sharedPreferences = requireContext().getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
            sharedPreferences.edit {
                putString("image_path", resizedImageFile?.absolutePath)
            }
        }
    }



    private var latestTmpUri: Uri? = null

    private fun takeImage() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                getTmpFileUri().let { uri ->
                    latestTmpUri = uri
                    selectImageFromGalleryResult.launch("image/*")

                    val sharedPreferences = requireContext().getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                    sharedPreferences.edit {
                        putString("image_uri", uri.toString())
                    }
                }
            }
        }
    }

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png", requireContext().cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(
            requireContext().applicationContext, "${requireContext().packageName}.provider",
            tmpFile
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}