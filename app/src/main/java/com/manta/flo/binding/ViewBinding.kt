package com.manta.flo.binding

import android.view.View
import android.widget.Toast
import androidx.databinding.BindingAdapter

object ViewBinding {
    @JvmStatic
    @BindingAdapter("toast")
    fun bindToast(view: View, text: String?) {
        text?.let {
            Toast.makeText(view.context, it, Toast.LENGTH_SHORT).show()
        }
    }
}