package com.example.shopme.Ui.activities

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shopme.R
import com.example.shopme.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences: SharedPreferences? =getSharedPreferences(Constants.SHOPME_PREFERENCES,Context.MODE_PRIVATE)
        val username= sharedPreferences!!.getString(Constants.SHOPME_USERNAME,"")
        tv_hello_username.text="Hello ${username}"
    }
}