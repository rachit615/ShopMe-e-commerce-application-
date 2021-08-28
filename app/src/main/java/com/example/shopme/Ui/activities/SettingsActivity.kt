package com.example.shopme.Ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.shopme.Firestore.FirestoreClass
import com.example.shopme.R
import com.example.shopme.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth

import com.example.shopme.models.User
import com.example.shopme.utils.Constants
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity(), View.OnClickListener {
    private lateinit var mUserDetails:User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setupActionBar()
        tv_edit.setOnClickListener(this)
        btn_logout.setOnClickListener(this)
        ll_address.setOnClickListener(this)

    }

     fun setupActionBar(){
        setSupportActionBar(toolbar_settings_activity)
        val actionBar=supportActionBar
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_back_ios)
        }
        toolbar_settings_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }
    override fun onResume() {
        super.onResume()
        getUserDetails()
    }
    private fun getUserDetails(){
        showProgressDialog("Please wait...")
        FirestoreClass().getUserDetails(this)

    }
    fun userDetailsSuccess(user:User){
        mUserDetails=user
        hideProgressDialog()
        GlideLoader(this@SettingsActivity).loadUserPicture(user.image,iv_user_photo)
        tv_name.text="${user.firstname} ${user.lastname}"
        tv_gender.text=user.gender
        tv_email.text=user.email
        tv_mobile_number.text="${user.mobile}"
    }

    override fun onClick(v: View?) {
        if(v!=null){
            when(v.id){
                R.id.btn_logout->{
                    showProgressDialog("Logging you out...")
                    FirebaseAuth.getInstance().signOut()

                    val intent=Intent(this@SettingsActivity,LoginActivity::class.java)
                    intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                R.id.tv_edit->{
                    val intent=Intent(this@SettingsActivity,User_profileActivity::class.java)
                    intent.putExtra(Constants.USER_EXTRA_DETAILS,mUserDetails)
                    startActivity(intent)


                }
                R.id.ll_address->{
                    val intent=Intent(this@SettingsActivity,AddressListActivity::class.java)
                    startActivity(intent)


                }
            }
        }
    }
}