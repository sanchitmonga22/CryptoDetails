package com.example.cryptodetails.ui.myAccount

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptodetails.data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MyAccountViewModel : ViewModel() {
    val profileImageUri: MutableStateFlow<Uri> =
        MutableStateFlow(Uri.parse("https://picsum.photos/200/300?random=1"))

    fun updateImageUri(uri: Uri) {
        this@MyAccountViewModel.profileImageUri.value = uri
        viewModelScope.launch {
            this@MyAccountViewModel.profileImageUri.emit(uri)
        }
        Repository.saveImage(uri)
    }
}