package com.example.cryptodetails.ui.myAccount

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptodetails.data.Repository
import com.example.cryptodetails.util.ContextHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MyAccountViewModel : ViewModel() {
    val profileImageUri: MutableStateFlow<Uri> =
        MutableStateFlow(Uri.parse("https://picsum.photos/200/300?random=1"))

    fun updateAndSaveImage(uri: Uri, byteArray: ByteArray?) {
        byteArray?.let {
            Repository.saveImage(uri, byteArray) { imageWasUploadedSuccessfully ->
                viewModelScope.launch(Dispatchers.Main) {
                    if (imageWasUploadedSuccessfully) {
                        // FIXME: updates are not applied immeditely.
                        this@MyAccountViewModel.profileImageUri.value = uri
                        this@MyAccountViewModel.profileImageUri.emit(uri)
                    } else {
                        Toast.makeText(
                            ContextHolder.context?.applicationContext,
                            "Image upload failed",
                            Toast.LENGTH_LONG
                        )
                        Log.d("MyAccountVM", "updateAndSaveImage: FAILEDDDDD ")
                    }
                }
            }
        }
    }
}