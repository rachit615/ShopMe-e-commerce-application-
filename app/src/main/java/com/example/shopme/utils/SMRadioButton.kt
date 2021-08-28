package com.example.shopme.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton

class SMRadioButton(context: Context,attributeSet: AttributeSet):AppCompatRadioButton(context,attributeSet) {
    init {
        applyFont()
    }
    private fun applyFont(){
        val typefaceRegular: Typeface = Typeface.createFromAsset(context.assets,"Montserrat-Bold.ttf")
        setTypeface(typefaceRegular)
    }
}