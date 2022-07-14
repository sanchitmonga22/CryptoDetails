package com.example.cryptodetails.ui.myAccount

import android.Manifest
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
//                requestStoragePermission()
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
            takePicture!!.launch(uri)
        } catch (ex: ActivityNotFoundException) {
            ex.printStackTrace()
        }

    }

    private lateinit var uri: Uri

    private val takePicture =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSaved ->
            Toast.makeText(context, "GLIDE IS HERE", Toast.LENGTH_LONG).show()
            if (isSaved) {
                Glide
                    .with(requireContext())
                    .load(uri)
                    .into(binding.profileImage)
            }
        }


//    private fun launchCameraIntent() {
//        Toast.makeText(context, "Camera Selected", Toast.LENGTH_LONG).show()
//        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        try {
//            cameraIntentResultLauncher.launch(cameraIntent)
//        } catch (e: ActivityNotFoundException) {
//            Toast.makeText(context, "Intent not working", Toast.LENGTH_LONG).show()
//        }
//    }

    private val cameraIntentResultLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            // should use ActivityContract. the new APIs to get the image uri
            // use glide to laod the image into the imageView

                result ->
            // TODO: save this in the viewmodel and call it in the activity and add the image
            binding.profileImage.setImageBitmap(result as Bitmap)
        }

//    private val galleryIntentResultLauncher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
////                val selectedImageURI: Uri = result.data.getpa
//                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
//                val cursor: Cursor? = activity?.getContentResolver()?.query(
//
//                    selectedImageURI,
//                    filePathColumn, null, null, null
//                )
//                cursor?.moveToFirst()
//
//                val columnIndex: Int? = cursor?.getColumnIndex(filePathColumn[0])
//                val picturePath: String? = cursor?.getString(columnIndex!!)
//                cursor?.close()
//
//                binding.profileImage.setImageBitmap(BitmapFactory.decodeFile(picturePath!!))
//            }
//        }

    private fun launchGalleryIntent() {
        Toast.makeText(context, "Gallery Selected", Toast.LENGTH_LONG).show()
        Intent("image/*").setAction(Intent.ACTION_GET_CONTENT)
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
//        const val CAMERA_INTENT_RESULT_CODE = 5
//        const val GALLERY_INTENT_RESULT_CODE = 6
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