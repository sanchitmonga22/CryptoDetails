package com.example.cryptodetails.ui.myAccount

import android.Manifest
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.cryptodetails.R
import com.example.cryptodetails.databinding.FragmentMyAccountBinding
import kotlinx.coroutines.flow.collectLatest
import java.io.File

class MyAccountFragment : Fragment() {

    private var _binding: FragmentMyAccountBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val myAccountViewModel = ViewModelProvider(this)[MyAccountViewModel::class.java]

        _binding = FragmentMyAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.profileImage.setOnClickListener {
            showImagePickingOptionsDialog()
        }

        lifecycleScope.launchWhenCreated {
            myAccountViewModel.text.collectLatest {

            }
        }
        return root
    }

    private fun showImagePickingOptionsDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.image_picker_dialog_title)
        builder.setMessage(R.string.image_picker_dialog_msg)
        builder.setPositiveButton("Gallery") { dialog, which ->
            if (!checkStoragePermission()) {
                requestStoragePermission()
            } else {
                launchGalleryIntent()
                dialog.dismiss()
            }
        }
        builder.setNegativeButton("Camera") { dialog, which ->
            if (!checkCameraPermission()) { // || !checkStoragePermission()
                requestCameraPermission()
            } else {
                launchCameraIntent()
                dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun launchCameraIntent() {
        val photoFile = File.createTempFile(
            "IMG_",
            ".jpg",
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )
        val authorityName = "${requireContext().packageName}.provider"
        uri = FileProvider.getUriForFile(
            requireContext(),
            authorityName,
            photoFile
        )
        try {
            clickPictureActivityResult.launch(
                FileProvider.getUriForFile(
                    requireContext(),
                    authorityName,
                    photoFile
                )
            )
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(context, "An error occurred with the CAMERA intent", Toast.LENGTH_LONG)
        }
    }

    private lateinit var uri: Uri

    private val clickPictureActivityResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSaved ->
            if (isSaved) {
                Glide.with(requireContext())
                    .load(uri)
                    .into(binding.profileImage)
            }
        }

    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            Glide.with(requireContext())
                .load(uri)
                .into(binding.profileImage)
        }

    private fun launchGalleryIntent() {
        Toast.makeText(context, "Gallery Selected", Toast.LENGTH_LONG).show()
        try {
            selectImageFromGalleryResult.launch("image/*")
        } catch (ex: ActivityNotFoundException) {
            ex.printStackTrace()
        }
    }

    private fun requestStoragePermission() {
        activity?.requestPermissions(
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            WRITE_EXTERNAL_PERMISSION_REQUEST_CODE
        )
        Toast.makeText(context, "Storage Permission requested", Toast.LENGTH_LONG).show()
    }

    private fun requestCameraPermission() {
        activity?.requestPermissions(
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
        Toast.makeText(context, "Camera Permission requested", Toast.LENGTH_LONG).show()
    }

    companion object {
        const val CAMERA_PERMISSION_REQUEST_CODE = 100
        const val WRITE_EXTERNAL_PERMISSION_REQUEST_CODE = 101
    }

    private fun checkCameraPermission() = ContextCompat.checkSelfPermission(
        requireActivity(),
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    private fun checkStoragePermission() = ContextCompat.checkSelfPermission(
        requireActivity(),
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}