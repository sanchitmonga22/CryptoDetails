package com.example.cryptodetails.ui.myAccount

import android.Manifest
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
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
import com.example.cryptodetails.R
import com.example.cryptodetails.databinding.FragmentMyAccountBinding
import java.io.File
import java.io.IOException

class MyAccountFragment : Fragment() {

    private var _binding: FragmentMyAccountBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var myAccountViewModel: MyAccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        myAccountViewModel = ViewModelProvider(this)[MyAccountViewModel::class.java]
        _binding = FragmentMyAccountBinding.inflate(inflater, container, false)
        binding.viewModel = myAccountViewModel

        val root: View = binding.root

        binding.profileImage.setOnClickListener {
            showImagePickingOptionsDialog()
        }
        return root
    }

    private fun showImagePickingOptionsDialog() {
        AlertDialog.Builder(context)
            .setTitle(R.string.image_picker_dialog_title)
            .setMessage(R.string.image_picker_dialog_msg)
            .setPositiveButton(R.string.image_picker_dialog_gallery_option) { dialog, _ ->
                if (!checkStoragePermission()) {
                    requestStoragePermission()
                } else {
                    launchGalleryIntent()
                    dialog.dismiss()
                }
            }
            .setNegativeButton(R.string.image_picker_dialog_camera_option) { dialog, _ ->
                if (!checkCameraPermission()) {
                    requestCameraPermission()
                } else {
                    launchCameraIntent()
                    dialog.dismiss()
                }
            }
            .show()
    }

    // region Camera and Gallery intents

    private fun launchCameraIntent() {
        val photoFile = File.createTempFile(
            "IMG_",
            ".jpg",
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )
        val authorityName = "${requireContext().packageName}.provider"
        uri = FileProvider.getUriForFile(requireContext(), authorityName, photoFile)
        try {
            clickPictureActivityResult.launch(
                FileProvider.getUriForFile(
                    requireContext(),
                    authorityName,
                    photoFile
                )
            )
        } catch (ex: ActivityNotFoundException) {
            ex.printStackTrace()
        }
    }

    private lateinit var uri: Uri

    private val clickPictureActivityResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSaved ->
            if (isSaved) {
                lifecycleScope.launchWhenStarted {
                    runCatching {
                        readBytes(requireContext(), uri)
                    }.onSuccess {
                        myAccountViewModel.updateAndSaveImage(uri, it)
                    }.onFailure {
                        it.printStackTrace()
                    }
                }
            }
        }

    @Throws(IOException::class)
    private fun readBytes(context: Context, uri: Uri): ByteArray? =
        context.contentResolver.openInputStream(uri)?.buffered()?.use { it.readBytes() }

    private val selectImageFromGalleryActivityResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            lifecycleScope.launchWhenStarted {
                kotlin.runCatching {
                    readBytes(context!!, uri!!)
                }.onSuccess {
                    myAccountViewModel.updateAndSaveImage(uri!!, it)
                }.onFailure {
                    it.printStackTrace()
                }
            }
        }

    private fun launchGalleryIntent() {
        try {
            selectImageFromGalleryActivityResult.launch("image/*")
        } catch (ex: ActivityNotFoundException) {
            ex.printStackTrace()
        }
    }

    // endregion

    // region Permissions
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

    // endregion

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}