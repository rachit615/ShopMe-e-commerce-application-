package com.example.shopme.Ui.activities

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.example.shopme.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_forgot_password.et_email
import kotlinx.android.synthetic.main.activity_register.*

class ForgotPasswordActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        btn_submit.setOnClickListener{
            val email:String=et_email.text.toString().trim{it<=' '}

                if(email.isEmpty()){
                    Toast.makeText(this,"Please Enter your E-mail address",Toast.LENGTH_LONG).show()
                }
                else{
                    showProgressDialog("Please wait")
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(this){
                        Task->
                        hideProgressDialog()
                        if(Task.isSuccessful){
                           Toast.makeText(this,"we have sent an e-mail to you to reset your Password",Toast.LENGTH_LONG).show()
                            finish()
                        }
                        else{
                            showErrorSnackBar(Task.exception?.message.toString(),true)
                        }
                    }
                }
        }
    }
    //back button on pressed (<--)go back
    private fun setActionBar(){
        setSupportActionBar(toolbar_forgot_password_activity)
        val actionBar=supportActionBar
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_black)
        }
        toolbar_register_activity.setNavigationOnClickListener{onBackPressed()}
    }
}