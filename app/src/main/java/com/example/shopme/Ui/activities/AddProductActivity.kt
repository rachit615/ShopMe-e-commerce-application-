package com.example.shopme.Ui.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.shopme.Firestore.FirestoreClass
import com.example.shopme.R
import com.example.shopme.models.Product
import com.example.shopme.utils.Constants
import com.example.shopme.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_settings.iv_user_photo
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException

class AddProductActivity : BaseActivity(),View.OnClickListener {
    private var mSelectedImageFileUri: Uri?=null
    private var mProductImageURL:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        setupActionBar()
        iv_add_update_product.setOnClickListener(this)
        btn_add_product.setOnClickListener(this)
    }
    fun setupActionBar(){
        setSupportActionBar(toolbar_add_product_activity)
        val actionBar=supportActionBar
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_back_ios)
        }
        toolbar_add_product_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }


    override fun onClick(v: View?) {
        if(v!=null){
            when(v.id){
                R.id.iv_add_update_product->{
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                        Constants.showImageChooser(this@AddProductActivity)
                    }
                    else{
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), Constants.READ_EXTERNAL_STORAGE_PERMISSION_CODE)
                    }
                }
                R.id.btn_add_product->{
                    if(validateProductDetails()){
                        uploadProductImage()
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==Constants.READ_EXTERNAL_STORAGE_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Constants.showImageChooser(this@AddProductActivity)
//                showErrorSnackBar("permission granted by you",false)
            }
            else{
                showErrorSnackBar("Hey,in order to Select an Image u have to allow Permissions",true)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK && requestCode==Constants.PICK_IMAGE_REQUEST_CODE){
            if(data!=null) {
                //sets a drawable to this image view
                iv_add_update_product.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_vector_edit))
                try {
                    mSelectedImageFileUri=data.data!!
                    GlideLoader(this).loadUserPicture(mSelectedImageFileUri!!,iv_product_image)
                    tv_add_image_of.visibility=View.GONE
                    iv_add_update_product.visibility= View.GONE
                }
                catch (e:IOException){
                    e.printStackTrace()
                }
            }
        }
    }
    private fun validateProductDetails():Boolean{
       return when{
           mSelectedImageFileUri==null->{
               showErrorSnackBar("Please insert Product image",true)
               return false
           }
           TextUtils.isEmpty(et_product_title.text.toString().trim { it<=' ' })->{
               showErrorSnackBar("Please Enter Product title",true)
               return false
           }
           TextUtils.isEmpty(et_product_price.text.toString().trim { it<=' ' })->{
               showErrorSnackBar("Please Enter Product price",true)
               return false
           }
           TextUtils.isEmpty(et_product_description.text.toString().trim { it<=' ' })->{
               showErrorSnackBar("Please Enter Product description",true)
               return false
           }
           TextUtils.isEmpty(et_product_quantity.text.toString().trim { it<=' ' })->{
               showErrorSnackBar("Please Enter Product quantity ",true)
               return false
           }
           else -> return true
       }
    }
    private fun uploadProductImage(){
        showProgressDialog("Please wait...")
        FirestoreClass().uploadImagetoCloudStorage(this,mSelectedImageFileUri,Constants.PRODUCT_IMAGE)
    }
    fun imageUploadedSuccess(imageUrl:String){
        mProductImageURL=imageUrl
        uploadProductDetails()
    }
    private fun uploadProductDetails(){
        val user_name=this.getSharedPreferences(Constants.SHOPME_PREFERENCES,Context.MODE_PRIVATE)
                .getString(Constants.SHOPME_USERNAME,"")!!
        val product=Product(
                FirestoreClass().getCurrentUserID(),
                user_name,
                et_product_title.text.toString().trim { it<=' ' },
                et_product_price.text.toString().trim { it<=' ' },
                et_product_description.text.toString().trim { it<=' ' },
                et_product_quantity.text.toString().trim{it<=' '},
                mProductImageURL!!
        )
        FirestoreClass().uploadProductDetails(this@AddProductActivity,product)
    }
    fun productUploadSuccess(){
        hideProgressDialog()
        Toast.makeText(this,"Product has been uploaded Succesfully.",Toast.LENGTH_LONG).show()
        finish()
    }



}