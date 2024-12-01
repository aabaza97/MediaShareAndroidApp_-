package com.example.myapplication2.ui
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication2.adapters.HomePagerAdapter
import com.example.myapplication2.databinding.ActivityHomeBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import android.Manifest
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myapplication2.model.HomeViewModel
import com.example.myapplication2.service.RetrofitClient
import com.example.myapplication2.service.auth.TokenManager
import com.example.myapplication2.service.auth.UserInfoManager
import com.example.myapplication2.service.auth.repository.AuthRepository
import com.example.myapplication2.service.media.repository.MediaRepository
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var mediaRepository: MediaRepository
    private lateinit var homeViewModel: HomeViewModel

    // Permission launcher
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted, proceed with media selection
            showMediaSelectionDialog()
        } else {
            // Permission denied
            Toast.makeText(this, "Storage permission is required to select media", Toast.LENGTH_SHORT).show()
        }
    }

    // Gallery picker for images
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            // Handle image selection
            handleMediaSelection(it,false)
        }
    }

    // Gallery picker for videos
    private val videoPickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            // Handle video selection
            handleMediaSelection(it,true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the repository
        setupRepository()

        // Set up the view model with the repository
        homeViewModel = ViewModelProvider(
            this,
            HomeViewModel.provideFactory(mediaRepository)
        )[HomeViewModel::class.java]

        // Set up the ViewPager and TabLayout
        setupViewPager()

        // setup listeners
        setupListeners()

        homeViewModel.getUploads()

        // Set the action bar title
        supportActionBar?.title = "Media Share App"
    }

    private fun setupRepository() {
        // Initialize MediaRepository
        val sharedPreferences = getSharedPreferences("AuthPrefs", MODE_PRIVATE)
        val userInfoManager = UserInfoManager(sharedPreferences)
        val tokenManager = TokenManager(sharedPreferences)
        val authService = RetrofitClient.authApi
        val authRepository = AuthRepository(authService, tokenManager, userInfoManager)
        val mediaService = RetrofitClient.mediaApi
        mediaRepository = MediaRepository(mediaService, authRepository)
    }

    private fun setupViewPager() {
        viewPager = binding.viewPager
        tabLayout = binding.tabLayout

        val pagerAdapter = HomePagerAdapter(this)
        viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Home"
                1 -> "Profile"
                else -> ""
            }
        }.attach()
    }

    private fun setupListeners() {
        binding.fab.setOnClickListener {
            checkStoragePermission()
        }
    }

    private fun showMediaSelectionDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Select Media")
            .setItems(arrayOf("Select Image", "Select Video")) { _, which ->
                when (which) {
                    0 -> pickImage()
                    1 -> pickVideo()
                }
            }
            .show()
    }

    private fun pickImage() {
        imagePickerLauncher.launch("image/jpeg")
    }

    private fun pickVideo() {
        videoPickerLauncher.launch("video/mp4")
    }

    private fun handleMediaSelection(uri: Uri, isMovie: Boolean) {
        // Handle media selection
        Toast.makeText(this, "Selected media: $uri", Toast.LENGTH_SHORT).show()

        lifecycleScope.launch {
            try {
                val file = uri.toFile(this@HomeActivity)
                val response =  if (isMovie) {
                    mediaRepository.uploadVideo(file)
                } else {
                    mediaRepository.uploadImage(file)
                }

                when {
                    response.isSuccess -> {
                        response.getOrNull()?.let {
                            Log.d("HomeActivity", "Media uploaded successfully: $it")
                            homeViewModel.getUploads()
                        } ?: Log.e("HomeActivity", "Failed to upload media", Exception("Upload response is null"))
                    }
                    else -> {
                        throw response.exceptionOrNull() ?: Exception("Failed to upload media")
                    }
                }

            } catch (e: Exception) {
                Log.e("HomeActivity", "Failed to upload media", e)
                Toast.makeText(this@HomeActivity, "Failed to upload media", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkStoragePermission() {
        when {
            // For Android 13 (API 33) and above
            android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU -> {
                val imagePermission = ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                )
                val videoPermission = ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_VIDEO
                )

                if (imagePermission == PackageManager.PERMISSION_GRANTED &&
                    videoPermission == PackageManager.PERMISSION_GRANTED) {
                    showMediaSelectionDialog()
                } else {
                    // Request both permissions
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_VIDEO)
                }
            }

            // For Android 10 (API 29) to 12 (API 32)
            android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q -> {
                val permission = ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                if (permission == PackageManager.PERMISSION_GRANTED) {
                    showMediaSelectionDialog()
                } else {
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }

            // For Android 9 (API 28) and below
            else -> {
                val permission = ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                if (permission == PackageManager.PERMISSION_GRANTED) {
                    showMediaSelectionDialog()
                } else {
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        }
    }

    fun Uri.toFile(context: Context): File {
        // Check if the Uri scheme is content
        if (scheme == "content") {
            // Create a temporary file
            val contentResolver = context.contentResolver
            val mime = contentResolver.getType(this)
            val fileExtension = mime?.let {
                it.split("/")[1]
            } // TODO: Should be done using MimeTypeMap and ContentResolver

            val fileName = "temp_upload_file_${System.currentTimeMillis()}.${fileExtension ?: "tmp"}"
            val file = File(context.cacheDir, fileName)

            try {
                // Copy content to the temporary file and append file extension
                contentResolver.openInputStream(this)?.use { input ->
                    FileOutputStream(file).use { output ->
                        input.copyTo(output)
                    }
                }
                return file
            } catch (e: Exception) {
                Log.e("MediaUpload", "Error converting content URI to file", e)
                throw IOException("Could not create file from content URI", e)
            }
        }

        // If it's already a file URI, convert directly
        if (scheme == "file") {
            return File(path ?: throw IllegalArgumentException("Invalid file path"))
        }

        throw IllegalArgumentException("Unsupported URI scheme: $scheme")
    }


}