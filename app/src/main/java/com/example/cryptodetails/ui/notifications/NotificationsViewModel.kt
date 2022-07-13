package com.example.cryptodetails.ui.notifications

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NotificationsViewModel : ViewModel() {

    private val _text = MutableStateFlow<String>("This is notifications Fragment")
    val text: StateFlow<String> = _text
}