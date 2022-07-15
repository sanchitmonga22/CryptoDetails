package com.example.cryptodetails.util

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.cryptodetails.R
import com.google.android.material.textview.MaterialTextView

object CustomBindingAdapters {
    @JvmStatic
    @BindingAdapter("app:imageUriString")
    fun ImageView.loadImageFromUri(uri: Uri) {
        Glide.with(context)
            .load(uri)
            .error(R.drawable.ic_launcher_foreground)
            .into(this)
    }

    @JvmStatic
    @BindingAdapter("app:setText")
    fun MaterialTextView.setImageText(text: String) {
        this.text = text
    }
}