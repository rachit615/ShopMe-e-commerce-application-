package com.example.shopme.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.shopme.R
import java.io.IOException
import java.net.URI

class GlideLoader(val context: Context) {
    fun loadUserPicture(image:Any,imageview:ImageView){
        try {
            Glide
                .with(context)
                .load(image)
                .centerCrop()
                .placeholder(R.drawable.user_icon)
                .into(imageview)
        }
        catch (e:IOException){
            e.printStackTrace()
        }
    }

    fun loadProductPicture(image: Any, imageview: ImageView) {
        try{
            Glide
                    .with(context)
                    .load(image)
                    .centerCrop()
                    .into(imageview)
        }
        catch (e:IOException){
            e.printStackTrace()
        }
    }
}