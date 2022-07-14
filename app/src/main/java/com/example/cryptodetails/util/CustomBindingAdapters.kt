package com.example.cryptodetails.util

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object CustomBindingAdapters {
    @BindingAdapter("imageUri")
    fun ImageView.loadImage(uri: Uri?) {
        Glide.with(context)
            .load(uri)
            .into(this)
    }
}