package com.example.shopme.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class SMTextViewBold(context:Context, attrs:AttributeSet):AppCompatTextView(context,attrs){
    init {
        applyFont()
    }
    private fun applyFont(){
        val typefaceBold:Typeface= Typeface.createFromAsset(context.assets,"Montserrat-Bold.ttf")
        setTypeface(typefaceBold)
    }

}