package com.example.shopme.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {

    const val USERS:String="users"
    const val PRODUCTS:String="products"
    const val ORDERS:String="orders"
    const val PRODUCT_IMAGE="Product_Image"
    const val USER_ID:String="user_id"

    const val SHOPME_PREFERENCES:String="ShopMePrefs"
    const val SHOPME_USERNAME:String="logged_in_username"
    const val USER_EXTRA_DETAILS:String="UserExtraDetails"
    const val READ_EXTERNAL_STORAGE_PERMISSION_CODE=1
    const val PICK_IMAGE_REQUEST_CODE=2
    const val MOBILE:String="mobile"
    const val FIRST_NAME:String="firstname"
    const val LAST_NAME:String="lastname"
    const val MALE:String="male"
    const val FEMALE:String="female"
    const val GENDER:String="gender"
    const val IMAGE:String="image"
    const val USER_PROFILE_IMAGE="User_profile_image"
    const val EXTRA_PRODUCT_ID="extra_product_id"
    const val EXTRA_PRODUCT_OWNER_ID="extra_product_owner_id"
    const val DEFAULT_CART_QUANTITY:String="1"
    const val CART_QUANTITY:String="cart_quantity"
    const val STOCK_QUANTITY:String="stock_quantity"
    const val CART_ITEMS="cart_items"
    const val PRODUCT_ID="product_id"
    const val HOME="home"
    const val OFFICE="office"
    const val OTHER="other"
    const val ADDRESSES="addresses"
    const val EXTRA_SELECT_ADDRESS="extra_select_address"

    const val EXTRA_SELECTED_ADDRESS="extra_selected_address"


    fun  showImageChooser(activity: Activity) {//Intent(action,(this is uri))=>[mData=uri]
        val galleryIntent=Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryIntent,Constants.PICK_IMAGE_REQUEST_CODE)
    }
//android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    fun getFileExtension(activity: Activity, uri: Uri?):String?{
        //MimeTypeMap maps [extension : Mime Types]
        //getSingleton() -> get singleton instance of MimeType Map
        // getExtensionFromMimeType->get extension from Mimetype
        //contentResolver.getType(uri!!)-> return the Mime type for the content url
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}

//[

//]



