package com.example.cryptodetails.ui.myAccount

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptodetails.data.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MyAccountViewModel : ViewModel() {
    val profileImageUri: MutableStateFlow<Uri> =
        MutableStateFlow(Uri.parse("https://picsum.photos/200/300?random=1"))

    val imageText: MutableStateFlow<String> = MutableStateFlow("RANDOM_NAME")

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
                        this@MyAccountViewModel.imageText.value = uri.toString()
                    } else {
                        this@MyAccountViewModel.profileImageUri.value = emptyUri
                        this@MyAccountViewModel.imageText.value = "ERROR"
                    }
                }
            }
        }
    }
}