package com.example.shopme.Ui.activities

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.shopme.Firestore.FirestoreClass
import com.example.shopme.R
import com.example.shopme.models.User
import com.example.shopme.utils.Constants
import com.example.shopme.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.activity_user_profile.et_email
import kotlinx.android.synthetic.main.activity_user_profile.iv_user_photo
import java.io.IOException

class User_profileActivity :  View.OnClickListener,
    BaseActivity(){
    private lateinit var mUserDetails:User
    private var mSelectedImageFileUri:Uri?=null
    private var mUserProfileImageUrl:String=""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        //userDetails of type user .... BY intent & Parcelable taking details of User

        if(intent.hasExtra(Constants.USER_EXTRA_DETAILS)){
            mUserDetails= intent.getParcelableExtra(Constants.USER_EXTRA_DETAILS)!!
        }
        setupActionBar()
        GlideLoader(this).loadUserPicture(mUserDetails.image,iv_user_photo)
        et_first_name.setText(mUserDetails.firstname)
        et_last_name.setText(mUserDetails.lastname)
        et_email.isEnabled=false
        et_email.setText(mUserDetails.email)
        if(mUserDetails.mobile!=0L){
            et_mobile_number.setText(mUserDetails.mobile.toString())
        }
        if(mUserDetails.gender==Constants.MALE){
            rb_male.isChecked=true
        }
        else{
            rb_female.isChecked=true
        }
        iv_user_photo.setOnClickListener(this@User_profileActivity)

        btn_submit.setOnClickListener{
            showProgressDialog("Saving details...")

            if(mSelectedImageFileUri!=null)
            FirestoreClass().uploadImagetoCloudStorage(this@User_profileActivity,mSelectedImageFileUri,Constants.USER_PROFILE_IMAGE)
            else{
                updateUserProfileDetails()
            }
        }
    }
    private fun updateUserProfileDetails(){
        val userHashMap=HashMap<String,Any>()
        val firstName=et_first_name.text.toString().trim { it<=' ' }
        val lastName=et_last_name.text.toString().trim { it<=' ' }
        if(firstName!=mUserDetails.firstname){
            userHashMap[Constants.FIRST_NAME]=firstName
        }
        if(lastName!=mUserDetails.lastname){
            userHashMap[Constants.LAST_NAME]=lastName
        }

        val gender=if (rb_male.isChecked){
            Constants.MALE
        }
        else{
            Constants.FEMALE
        }
        if(gender.isNotEmpty() && gender!=mUserDetails.gender){
            userHashMap[Constants.GENDER]=gender
        }

        val mobileno=et_mobile_number.text.toString().trim{it<=' '}
        //If the mUserProfileImage
        if(mUserProfileImageUrl.isNotEmpty()){
            userHashMap[Constants.IMAGE]=mUserProfileImageUrl
        }
        if(mobileno.isNotEmpty() &&  mobileno!=mUserDetails.mobile.toString()){
            userHashMap[Constants.MOBILE]=mobileno.toLong()
        }

        FirestoreClass().updateUserDetails(this@User_profileActivity,userHashMap)
    }
    fun userProfileDetailsUpdatedSuccess(){
        hideProgressDialog()
        showErrorSnackBar("Profile details updated succesfully",false)

    }
    fun setupActionBar(){
        setSupportActionBar(toolbar_user_profile_activity)
        val actionBar=supportActionBar
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_back_ios)
        }
        toolbar_user_profile_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id){
                R.id.iv_user_photo->{
                    if(ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                        Constants.showImageChooser(this@User_profileActivity)
                    }
                    else{
                        ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE),Constants.READ_EXTERNAL_STORAGE_PERMISSION_CODE)
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==Constants.READ_EXTERNAL_STORAGE_PERMISSION_CODE){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Constants.showImageChooser(this@User_profileActivity)
//                showErrorSnackBar("permission granted by you",false)
            }
            else{
                showErrorSnackBar("Hey,in order to Select an Image u have to allow Permissions",true)
            }
        }


    }
// this will set the image on the [ box ] of image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==Activity.RESULT_OK && requestCode==Constants.PICK_IMAGE_REQUEST_CODE){
            if(data!=null) {
                try {
                    mSelectedImageFileUri=data.data!!
                    Glide
                        .with(this)
                        .load(mSelectedImageFileUri)
                        .centerCrop()
                        .placeholder(R.drawable.ic_user_placeholder)
                        .into(iv_user_photo)
                }
                catch (e:IOException){
                    e.printStackTrace()
                    Toast.makeText(this,"Failed to Select the Image",Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    fun imageUploadedSuccess(imageUrl:String){
        mUserProfileImageUrl=imageUrl
        updateUserProfileDetails()
    }





}

