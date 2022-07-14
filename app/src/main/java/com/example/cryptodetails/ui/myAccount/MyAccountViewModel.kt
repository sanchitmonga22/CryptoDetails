package com.example.cryptodetails.ui.myAccount

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptodetails.data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MyAccountViewModel : ViewModel() {
    val profileImageUri: MutableStateFlow<String> =
        MutableStateFlow("https://picsum.photos/200/300?random=1")

    fun updateImageUri(uri: String) {
        this@MyAccountViewModel.profileImageUri.value = uri
        viewModelScope.launch {
            this@MyAccountViewModel.profileImageUri.emit(uri)
        }
        Repository.saveImage(Uri.parse(uri))
    }
}