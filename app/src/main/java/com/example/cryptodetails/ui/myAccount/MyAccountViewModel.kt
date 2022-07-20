package com.example.cryptodetails.ui.myAccount

import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptodetails.R
import com.example.cryptodetails.app.CryptoApp
import com.example.cryptodetails.data.Repository
import com.example.cryptodetails.util.ContextHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MyAccountViewModel : ViewModel() {
    val profileImageUri: MutableStateFlow<Uri> =
        MutableStateFlow(Uri.parse("https://picsum.photos/200/300?random=1"))

    companion object {
        private val emptyUri = Uri.parse("")
    }

    fun updateAndSaveImage(uri: Uri, byteArray: ByteArray?) {
        byteArray?.let {
            Repository.saveImage(uri, byteArray) { wasUploadedSuccessfully ->
                viewModelScope.launch(Dispatchers.Main) {
                    if (wasUploadedSuccessfully) {
                        // FIXME: updates are not applied immeditely.
                        this@MyAccountViewModel.profileImageUri.value = uri
                    } else {
                        this@MyAccountViewModel.profileImageUri.value = emptyUri
                    }
                    notifyUserAboutUpload(byteArray, wasUploadedSuccessfully)
                }
            }
        }
    }

    private fun notifyUserAboutUpload(byteArray: ByteArray?, wasUploadSuccessful: Boolean) {
        NotificationManagerCompat.from(ContextHolder.context!!).notify(
            3,
            if (wasUploadSuccessful) getSuccessNotification(byteArray)
            else getFailureNotification()
        )
    }

    private fun getSuccessNotification(byteArray: ByteArray?) =
        NotificationCompat.Builder(ContextHolder.context!!, CryptoApp.CHANNEL_1)
            .setSmallIcon(com.google.android.material.R.drawable.ic_mtrl_checked_circle)
            .setContentTitle("Image uploaded")
            .setContentText("Image upload was successful")
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    ContextHolder.context?.resources,
                    R.drawable.cryptoo
                )
            )
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(BitmapFactory.decodeByteArray(byteArray, 0, byteArray?.size!!))
            )
            .setColor(Color.BLUE)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setOnlyAlertOnce(true)
            .build()

    private fun getFailureNotification() =
        NotificationCompat.Builder(ContextHolder.context!!, CryptoApp.CHANNEL_1)
            .setSmallIcon(R.drawable.error_loading_image)
            .setContentTitle("Image not uploaded")
            .setContentText("Image upload FAILED")
            .setStyle(
                NotificationCompat.BigPictureStyle().bigPicture(
                    BitmapFactory.decodeResource(
                        ContextHolder.context?.resources,
                        R.drawable.error_loading_image
                    )
                )
            )
            .setColor(Color.RED)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setOnlyAlertOnce(true)
            .build()
}