package com.example.cryptodetails.ui.myAccount

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MyAccountViewModel : ViewModel() {

    private val _text = MutableStateFlow("This is My Account Fragment")
    val text: StateFlow<String> = _text
}