package com.example.cryptodetails.ui.myAccount

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptodetails.data.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MyAccountViewModel : ViewModel() {
    val profileImageUri: MutableStateFlow<Uri> =
        MutableStateFlow(Uri.parse("https://picsum.photos/200/300?random=1"))

    fun updateAndSaveImage(uri: Uri, byteArray: ByteArray?) {
        byteArray?.let {
            Repository.saveImage(uri, byteArray) { wasUploadedSuccessfully ->
                viewModelScope.launch(Dispatchers.Main) {
                    if (wasUploadedSuccessfully) {
                        // FIXME: updates are not applied immeditely.
                        this@MyAccountViewModel.profileImageUri.value = uri
                        this@MyAccountViewModel.profileImageUri.emit(uri)
                    } else {
                        this@MyAccountViewModel.profileImageUri.value = Uri.parse("")
                        this@MyAccountViewModel.profileImageUri.emit(Uri.parse(""))
                        Log.d("MyAccountVM", "updateAndSaveImage: FAILEDDDDD ")
                    }
                }
            }
        }
    }
}