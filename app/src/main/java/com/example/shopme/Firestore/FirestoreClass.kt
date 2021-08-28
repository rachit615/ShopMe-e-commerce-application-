package com.example.shopme.Firestore
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.shopme.Ui.activities.*
import com.example.shopme.Ui.fragments.DashboardFragment
import com.example.shopme.Ui.fragments.OrdersFragment
import com.example.shopme.Ui.fragments.ProductsFragment
import com.example.shopme.models.*

import com.example.shopme.utils.Constants

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_products.*


class FirestoreClass {
    val mfirestore=FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo:User){
        //adding in the "users" collection
        mfirestore.collection(Constants.USERS)
                //setting the "id" for the unique thing
                .document(userInfo.id)
                //setting the data of the "unique" user
                .set(userInfo, SetOptions.merge())
                //on Success-> do
                .addOnSuccessListener {
                    activity.registerUserSuccess()
                }
                .addOnFailureListener{e->
                    activity.hideProgressDialog()
                    Log.e(
                            activity.javaClass.simpleName,"Error while Registering the user",e
                    )

                }
    }
    //get the current user id of userlogged in
    fun getCurrentUserID():String{
        val currentUser=FirebaseAuth.getInstance().currentUser
        var currentUserId=""
        if(currentUser!=null){
            currentUserId=currentUser.uid
        }
        return currentUserId
    }
    // In which Activity,we want to get the User Details
    fun getUserDetails(activity: Activity){
        mfirestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
                //.addOnSuccessListener is very I M P O RT A N T
            .addOnSuccessListener { document->
                Log.i(activity.javaClass.simpleName,document.toString())

                //HERE WE HAVE RECIEVED THE [Document] SNAPSHOT WHICH IS CONVERTED INTO USER DATA MODEL OBJECT
                val user=document.toObject(User::class.java)!!
                // How to get the username from login screen to main screen with the help of shared prefernces
                val sharedPreferences=activity.getSharedPreferences(
                        Constants.SHOPME_PREFERENCES,Context.MODE_PRIVATE
                )
                val editor=sharedPreferences.edit()
                editor.putString(Constants.SHOPME_USERNAME,"${user.firstname} ${user.lastname}")
                editor.apply()
                //Tasks accordingly Activities....
                when(activity){
                    is LoginActivity ->{
                        activity.loggedInSuccess(user)
                    }
                    is SettingsActivity->{
                        activity.userDetailsSuccess(user)
                    }
                }
            }
            //on failure... what to do??
            .addOnFailureListener{e->
                when(activity){
                    is LoginActivity->{
                        activity.hideProgressDialog()
                    }
                    is SettingsActivity->{
                        activity.hideProgressDialog()
                    }
                }
                Log.e(activity.javaClass.simpleName,"Error while Logging in the User")
            }
    }

    fun updateUserDetails(activity: Activity,userHashMap:HashMap<String,Any>){
        mfirestore.collection(Constants.USERS).document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                when(activity){
                    is User_profileActivity ->{
                        activity.userProfileDetailsUpdatedSuccess()
                    }
                }
            }
            .addOnFailureListener{e->
                when(activity){
                    is User_profileActivity ->{
                        activity.hideProgressDialog()
                    }
                }
                Log.e(activity.javaClass.simpleName,"Sorry...Error while updating details",e)
            }
    }

    fun uploadImagetoCloudStorage(activity: Activity,imageFileUri:Uri?,imageType:String){
        val sRef:StorageReference=FirebaseStorage.getInstance().reference.child(
            imageType+System.currentTimeMillis()+"."+
                    Constants.getFileExtension(activity,imageFileUri)
        )
        sRef.putFile(imageFileUri!!)
            .addOnSuccessListener { taskSnapshot ->
                //image uploaded succesfully here
            Log.e("image firebase url", taskSnapshot.metadata!!.reference!!.downloadUrl!!.toString())

            taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener{uri->
                Log.e("Downloadable image URL",uri.toString())
                when(activity){
                    is User_profileActivity ->{
                        activity.imageUploadedSuccess(uri.toString())
                    }
                    is AddProductActivity->{
                        activity.imageUploadedSuccess(uri.toString())
                    }
                }
            }
        }
            .addOnFailureListener{Exception->

                when(activity){
                    is User_profileActivity ->{
                        activity.hideProgressDialog()
                    }
                }
                Log.e(activity.javaClass.simpleName,Exception.message,Exception)
            }
    }
    fun uploadProductDetails(activity: AddProductActivity,productInfo:Product){
        mfirestore.collection(Constants.PRODUCTS)
                .document()
                .set(productInfo, SetOptions.merge())
                .addOnSuccessListener {
                    activity.productUploadSuccess()
                }
                .addOnFailureListener {e->
                    activity.hideProgressDialog()
                    Log.e(activity.javaClass.simpleName,"Error while uploading product details",e)
                }
    }

    fun getProductsList(fragment:Fragment){
        mfirestore.collection(Constants.PRODUCTS)
            .whereEqualTo(Constants.USER_ID,getCurrentUserID())
            .get()
            .addOnSuccessListener { document->
                Log.e("products list",document.documents.toString())
                val productsList: ArrayList<Product> =ArrayList()
                for(i in document.documents){
                    val product=i.toObject(Product::class.java)!!
                    product.product_id=i.id
                    productsList.add(product)
                }
                when(fragment){
                    is ProductsFragment->{
                        fragment.successProductsListFromFirestore(productsList)
                    }
                }
            }

    }

    fun getDashboardItemsList(fragment: DashboardFragment){
        mfirestore.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener {document->
                val productsList:ArrayList<Product> =ArrayList()
                for(i in document.documents){
                    val product:Product=i.toObject(Product::class.java)!!
                    product.product_id=i.id
                    productsList.add(product)
                }
                fragment.successProductsListFromFirestore(productsList)

            }
            .addOnFailureListener {e->
                fragment.hideProgressDialog()
                Log.e(fragment.javaClass.simpleName,"error while getting dashboard items")
            }
    }
    fun deleteProductsFromFirestore(fragment: ProductsFragment,productID:String){
        mfirestore.collection(Constants.PRODUCTS)
            .document(productID)
            .delete()
            .addOnSuccessListener {
                fragment.successOnProductDelete()
            }
            .addOnFailureListener {e->
                Log.e(fragment.requireActivity().javaClass.simpleName,"Error while removing your product!!")

            }
    }
    fun getProductDetailsFromFirestore(activity: ProductDetailsActivity,productID: String){
        mfirestore.collection(Constants.PRODUCTS)
                .document(productID)
                .get()
                .addOnSuccessListener { document->
                    val product=document.toObject(Product::class.java)!!
                    activity.getproductDetailsSuccess(product)
                }
                .addOnFailureListener {e->
                    activity.hideProgressDialog()
                    e.printStackTrace()
                }
    }
    fun addCartItems(activity: ProductDetailsActivity, addToCart: CartItem) {
        mfirestore.collection(Constants.CART_ITEMS)
            .document()
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(addToCart, SetOptions.merge())
            .addOnSuccessListener {
                activity.addToCartSuccess()
            }
            .addOnFailureListener { e ->

                activity.hideProgressDialog()

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while creating the document for cart item.",
                    e
                )
            }
    }
    fun checkIfItemExistInCart(activity: ProductDetailsActivity,productID: String){
        mfirestore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID,getCurrentUserID())
            .whereEqualTo(Constants.PRODUCT_ID,productID)
            .get()
            .addOnSuccessListener { document->
                Log.e(activity.javaClass.simpleName,document.documents.toString())
                if(document.documents.size>0){
                    activity.productExistsInCart()
                }
                else{
                    activity.hideProgressDialog()
                }


            }
            .addOnFailureListener { e->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,"Error while checking the existing cart list")
            }
    }
    fun getCartList(activity: Activity){
        mfirestore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID,getCurrentUserID())
            .get()
            .addOnSuccessListener { document->
                Log.e(activity.javaClass.simpleName,document.documents.toString())
                val list:ArrayList<CartItem> =ArrayList()
                for(i in document.documents){
                    val cartItem=i.toObject(CartItem::class.java)!!
                    cartItem.id=i.id
                    list.add(cartItem)
                }
                when(activity){
                    is CartListActivity->{
                        activity.successCartItemList(list)
                    }
                    is CheckoutActvity->{
                        activity.successCartListFromFirestore(list)
                    }
                }

            }
            .addOnFailureListener {e->
                when(activity){
                    is CartListActivity->{
                        activity.hideProgressDialog()
                    }
                }
                Log.e(activity.javaClass.simpleName,"Error while getting cart list items.",e)

            }
    }
    fun getAllProductsList(activity: Activity){
        mfirestore.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener {document->
                val productsList:ArrayList<Product> = ArrayList()

                for(i in document.documents){
                    val product=i.toObject(Product::class.java)
                    product!!.product_id=i.id
                    productsList.add(product)
                }
                when(activity){
                    is CartListActivity->{
                        activity.successProductsListFromFirestore(productsList)
                    }
                    is CheckoutActvity->{
                        activity.successProductsListFromFirestore(productsList)

                    }
                }
            }
            .addOnFailureListener {e->
                when(activity){
                    is CartListActivity->{
                        activity.hideProgressDialog()
                    }
                    is CheckoutActvity->{
                        activity.hideProgressDialog()
                    }
                }
                Log.e("Get products list","Error while getting all products list..")

            }
    }
    fun removeItemFromCart(context: Context, cart_id: String) {
        mfirestore.collection(Constants.CART_ITEMS)
                .document(cart_id)
                .delete()
                .addOnSuccessListener {
                    when (context) {
                        is CartListActivity -> {
                            context.itemRemovedSuccess()
                        }
                    }
                }
                .addOnFailureListener { e ->
                    when (context) {
                        is CartListActivity -> {
                            context.hideProgressDialog()
                        }
                    }
                    Log.e(context.javaClass.simpleName,"Error while removing cart item from cart list.")
                }
    }
    fun updateMyCart(context: Context,cart_id: String,itemHashMap: HashMap<String,Any>){
        mfirestore.collection(Constants.CART_ITEMS)
                .document(cart_id)
                .update(itemHashMap)
                .addOnSuccessListener {
                    when(context){
                        is CartListActivity->{
                            context.itemUpdateSuccess()
                        }
                    }


                }
                .addOnFailureListener {e->
                    when(context){
                        is CartListActivity->{
                            context.hideProgressDialog()
                        }
                    }
                    Log.e(context.javaClass.simpleName,"Error while updating cart.")
                }
    }
    fun addAddress(activity: AddEditAddressActivity, addressInfo: Address) {
        mfirestore.collection(Constants.ADDRESSES)
            .document()
            .set(addressInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.addUpdateAddressSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,"Error while adding address.",e)
            }
    }
    fun getAddressesList(activity: AddressListActivity) {
        mfirestore.collection(Constants.ADDRESSES)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e(activity.javaClass.simpleName, document.documents.toString())
                val addressList: ArrayList<Address> = ArrayList()
                for (i in document.documents) {
                    val address = i.toObject(Address::class.java)!!
                    address.id = i.id
                    addressList.add(address)
                }
                activity.successAddressListFromFirestore(addressList)
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while getting the address list.", e)
            }
    }
    fun placeOrder(activity: CheckoutActvity, order: Order) {
        mfirestore.collection(Constants.ORDERS)
            .document()
            .set(order, SetOptions.merge())
            .addOnSuccessListener {
                activity.orderPlacedSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,"Error while placing an order",e)
            }
    }
    fun updateAllDetails(activity: CheckoutActvity,cartList:ArrayList<CartItem>){
        val writeBatch=mfirestore.batch()
        for(item in cartList){
            val productHashMap=HashMap<String,Any>()
            productHashMap[Constants.STOCK_QUANTITY]=(item.stock_quantity.toInt()-item.cart_quantity.toInt()).toString()
            val documentReference=mfirestore.collection(Constants.PRODUCTS).document(item.product_id)
            writeBatch.update(documentReference,productHashMap)
        }
        for (item in cartList){
            val documentReference=mfirestore.collection(Constants.CART_ITEMS).document(item.id)
            writeBatch.delete(documentReference)
        }
        writeBatch.commit().addOnSuccessListener {
            activity.allDetailsUpdatedSuccessfully()
        }.addOnFailureListener { e ->
            activity.hideProgressDialog()
            Log.e(activity.javaClass.simpleName,"Error while updating all details after placing an order.",e)
        }
    }
    fun getMyOrdersList(fragment: OrdersFragment){
        mfirestore.collection(Constants.ORDERS)
            .whereEqualTo(Constants.USER_ID,getCurrentUserID())
            .get()
            .addOnSuccessListener {document->
                val ordersList:ArrayList<Order> =ArrayList<Order>()
                for (i in document.documents){
                    val orderItem=i.toObject(Order::class.java)!!
                    orderItem.id=i.id
                    ordersList.add(orderItem)
                }
                fragment.showOrdersListSuccess(ordersList)
            }.addOnFailureListener { e->
                fragment.hideProgressDialog()
                Log.e(fragment.javaClass.simpleName,"Error while getting orders list.",e)

            }
    }
}