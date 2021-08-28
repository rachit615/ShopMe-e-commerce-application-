package com.example.shopme.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class SMEditText(context: Context, attributeSet: AttributeSet)
    :AppCompatEditText(context,attributeSet){
    init {
        applyFont()
    }
    private fun applyFont(){
        val typefaceRegular: Typeface = Typeface.createFromAsset(context.assets,"Montserrat-Regular.ttf")
        setTypeface(typefaceRegular)
    }
    

}
