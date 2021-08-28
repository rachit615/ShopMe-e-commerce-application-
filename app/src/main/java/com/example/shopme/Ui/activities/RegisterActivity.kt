package com.example.shopme.Ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import com.example.shopme.Firestore.FirestoreClass
import com.example.shopme.R
import com.example.shopme.models.User
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.et_email
import kotlinx.android.synthetic.main.activity_register.et_password
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setActionBar()

        tv_login.setOnClickListener(){
            val intent=Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        btn_register.setOnClickListener{
                registerUser()
        }
}



    //back button on pressed (<--)go back
    private fun setActionBar(){
        setSupportActionBar(toolbar_register_activity)
        val actionBar=supportActionBar
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_black)
        }
        toolbar_register_activity.setNavigationOnClickListener{onBackPressed()}
    }
    /* A function to validate the entries of a new user.
    */
    private fun validateRegisterDetails():Boolean {
         return when {
            TextUtils.isEmpty(et_first_name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                 false

            }

            TextUtils.isEmpty(et_last_name.text.toString().trim() { it == ' ' }) -> {
                showErrorSnackBar(getString(R.string.err_msg_enter_last_name), true)
                 false
            }

            TextUtils.isEmpty(et_email.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                 false
            }

            TextUtils.isEmpty(et_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            TextUtils.isEmpty(et_confirm_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_confirm_password),
                    true
                )
                 false
            }

            et_password.text.toString().trim { it <= ' ' } != et_confirm_password.text.toString()
                .trim { it <= ' ' } -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_password_and_confirm_password_mismatch),
                    true
                )
                 false
            }

            !cb_terms_and_condition.isChecked -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_agree_terms_and_condition),
                    true
                )
                 false
            }
//             else->{true}
             else -> true
         }

    }

    //function to register user and his details
    private fun registerUser(){
        if (validateRegisterDetails()){
            //show progress dialog screen here
            showProgressDialog("Please wait")
            //collect email and password hrere
        val email:String=et_email.text.toString().trim{it<=' '}
        val password:String=et_password.text.toString().trim{it<=' '}
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(this){
            task ->
            //task will be completed here
            //hiding the progrees dialog

            hideProgressDialog()
            if(task.isSuccessful){
                val firebaseUser:FirebaseUser= task.result!!.user!!
                val id=firebaseUser.uid

                val user=User(
                    id,
                    et_first_name.text.toString().trim{it<=' '},
                    et_last_name.text.toString().trim{it<=' '},
                    et_email.text.toString().trim{it<=' '}
                )
                FirestoreClass().registerUser(this@RegisterActivity,user)


//                FirebaseAuth.getInstance().signOut()
//                finish()
            }
            else{
                showErrorSnackBar(task.exception!!.message.toString(),false)
            }
        }
        }
    }

    fun registerUserSuccess(){
        hideProgressDialog()
        showErrorSnackBar("You are Registered Succesfully",false)
        val intent=Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

}