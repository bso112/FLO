package com.manta.flo.binding

import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object ViewBinding {
    @JvmStatic
    @BindingAdapter("toast")
    fun bindToast(view: View, text: String?) {
        text?.let {
            Toast.makeText(view.context, it, Toast.LENGTH_SHORT).show()
        }
    }

    @JvmStatic
        @BindingAdapter("image_uri")
    fun getImageFromUri(view: View, uri: String?) {
        if (view !is ImageView) return;
        uri?.let {
            Glide.with(view).load(uri).into(view);
        }
    }


}