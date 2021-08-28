package com.example.shopme.Ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import com.example.shopme.Firestore.FirestoreClass
import com.example.shopme.R
import com.example.shopme.models.User
import com.example.shopme.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

 class
 LoginActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        btn_login.setOnClickListener(this)
        tv_register.setOnClickListener(this)
        tv_forgot_password.setOnClickListener(this)

    }
     override fun onClick(view: View){
         if(view!=null){
             when(view.id){
                 R.id.btn_login->{
                     loginRegisteredUser()
                 }
                 R.id.tv_register->{
                     val intent=Intent(this,
                         RegisterActivity::class.java)
                     startActivity(intent)
                 }
                 R.id.tv_forgot_password->{

                     val intent=Intent(this,
                         ForgotPasswordActivity::class.java)
                     startActivity(intent)
                 }

             }
         }
     }

     private fun validateLoginDetails():Boolean {
         return when{
             TextUtils.isEmpty(et_email.text.toString().trim{it<=' '})->{
                 showErrorSnackBar("Please enter your E-mail",true)
              return  false
             }
             TextUtils.isEmpty(et_password.text.toString().trim{it<=' '})->{
                 showErrorSnackBar("Please enter Password",true)
                 return false
             }

             else -> {
                 return true
             }
         }
     }

     private fun loginRegisteredUser(){
         if(validateLoginDetails()){
             showProgressDialog("Logging you in...")

             val email:String=et_email.text.toString().trim{it<=' '}
             val password:String=et_password.text.toString().trim{it<=' '}

             FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener{
                 task ->
                 if(task.isSuccessful){
                     FirestoreClass().getUserDetails(this@LoginActivity)
                 }
                 else{
                     showErrorSnackBar(task.exception?.message.toString(),true)
                 }
             }
         }
     }

     fun loggedInSuccess(user:User){
         //hdie the progress dialog
         hideProgressDialog()
         // Print the details of the current user that is logged in...
         showErrorSnackBar("You are Succesfully logged in",false)
         val intent=Intent(this@LoginActivity,
             DashboardActivity::class.java)
         intent.putExtra(Constants.USER_EXTRA_DETAILS,user)
         startActivity(intent)
         finish()

     }


 }