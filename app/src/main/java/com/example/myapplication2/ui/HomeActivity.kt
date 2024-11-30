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

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

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
            handleMediaSelection(it, isImage = true)
        }
    }

    // Gallery picker for videos
    private val videoPickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            // Handle video selection
            handleMediaSelection(it, isImage = false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the ViewPager and TabLayout
        setupViewPager()

        // setup listeners
        setupListeners()

        // Set the action bar title
        supportActionBar?.title = "Media Share App"
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
//            showMediaSelectionDialog()
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
        imagePickerLauncher.launch("image/*")
    }

    private fun pickVideo() {
        videoPickerLauncher.launch("video/*")
    }

    private fun handleMediaSelection(uri: Uri, isImage: Boolean) {
        // Handle media selection
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
}