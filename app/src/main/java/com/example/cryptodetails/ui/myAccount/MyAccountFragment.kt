package com.example.cryptodetails.ui.myAccount

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.cryptodetails.R
import com.example.cryptodetails.databinding.FragmentMyAccountBinding
import kotlinx.coroutines.flow.collectLatest

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
            // select image from the gallery,
            // click a new image with the camera and select that
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
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Camera") { dialog, which ->
            if (!checkCameraPermission() || !checkStoragePermission()) {
                requestCameraPermission()
                requestStoragePermission()
            } else {
                launchCameraIntent()
            }
            dialog.dismiss()
        }
        builder.show()
    }

    private fun launchCameraIntent() {
        Toast.makeText(context, "Camera Selected", Toast.LENGTH_LONG).show()
    }

    private fun launchGalleryIntent() {
        Toast.makeText(context, "Gallery Selected", Toast.LENGTH_LONG).show()
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