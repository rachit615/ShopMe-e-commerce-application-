package com.example.shopme.Ui.activities

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.shopme.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_progress.*

open class BaseActivity : AppCompatActivity() {
     private lateinit var mProgressDialog:Dialog
    private var doublebacktoexitPressedOnce=false


    fun showErrorSnackBar(message:String,errorMessage:Boolean){
        val snackBar=Snackbar.make(findViewById(android.R.id.content),message,Snackbar.LENGTH_LONG)
        val snackBarView=snackBar.view
        if(errorMessage){
            snackBarView.setBackgroundColor(ContextCompat.getColor(this@BaseActivity,R.color.snackbarError))

        }
        else{
            snackBarView.setBackgroundColor(ContextCompat.getColor(this@BaseActivity,R.color.snackbarSuccess))

        }
        snackBar.show()
    }



     //function to show Progress Dialog
     fun showProgressDialog(text:String){
         mProgressDialog= Dialog(this)
         mProgressDialog.setContentView(R.layout.dialog_progress)
         mProgressDialog.tv_progress_text.text=text
         mProgressDialog.setCancelable(false)
         mProgressDialog.setCanceledOnTouchOutside(true)
         mProgressDialog.show()
     }
    fun hideProgressDialog(){
        mProgressDialog.dismiss()
    }



    fun doublebacktoExit(){
        if(doublebacktoexitPressedOnce){
            super.onBackPressed()
            return
        }
        this.doublebacktoexitPressedOnce=true
        Toast.makeText(this,resources.getString(R.string.please_click_back_again_to_exit),Toast.LENGTH_SHORT).show()
        Handler().postDelayed({doublebacktoexitPressedOnce=false},2000)

    }
}