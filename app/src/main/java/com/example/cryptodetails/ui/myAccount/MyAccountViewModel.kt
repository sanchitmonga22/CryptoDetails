package com.example.cryptodetails.ui.myAccount

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class MyAccountViewModel : ViewModel() {
    val profileImageUri = MutableStateFlow<Uri?>(null)
}