package com.example.shopme.Ui.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.shopme.Firestore.FirestoreClass
import com.example.shopme.R
import com.example.shopme.models.CartItem
import com.example.shopme.models.Product
import com.example.shopme.utils.Constants
import com.example.shopme.utils.GlideLoader
import com.example.shopme.utils.SMTextViewBold
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_product_details.*

class ProductDetailsActivity : BaseActivity(),View.OnClickListener {
    private var mProductID:String=""
    private lateinit var mProductDetails:Product
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        setupActionBar()
        if(intent.hasExtra(Constants.EXTRA_PRODUCT_ID)){
            mProductID= intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }
        var productOwnerID:String=""
        if(intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)){
            productOwnerID=intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
        }
        if(FirestoreClass().getCurrentUserID()==productOwnerID){
            btn_add_to_cart.visibility=View.GONE
        }
        else{
            btn_add_to_cart.visibility=View.VISIBLE
        }
        size_s.setOnClickListener(this)
        size_l.setOnClickListener(this)
        size_m.setOnClickListener(this)
        size_xl.setOnClickListener(this)
        btn_add_to_cart.setOnClickListener(this)
        btn_go_to_cart.setOnClickListener(this)
        getProductDetails()
    }
    fun setupActionBar(){
        setSupportActionBar(toolbar_product_details_activity)
        val actionBar=supportActionBar
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_back_keyboard)
        }
        toolbar_product_details_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }
    fun getProductDetails(){
        showProgressDialog("Hang on,loading content...")
        FirestoreClass().getProductDetailsFromFirestore(this@ProductDetailsActivity,mProductID)

    }
    fun getproductDetailsSuccess(product:Product){
        mProductDetails=product
        GlideLoader(this@ProductDetailsActivity).loadProductPicture(product.image,iv_product_detail_image)
        tv_product_details_title.text=product.title
        tv_product_details_price.text="Rs. ${product.price}"
        tv_product_details_description.text=product.description
        tv_product_details_stock_quantity.text=product.stock_quantity
        if(product.stock_quantity.toInt()==0){
            hideProgressDialog()
            btn_add_to_cart.visibility=View.GONE
            tv_product_details_stock_quantity.text="OUT OF STOCK"
        }
        else{
            if (FirestoreClass().getCurrentUserID()==product.user_id){
                hideProgressDialog()
            }
            else{
                FirestoreClass().checkIfItemExistInCart(this,mProductID)
            }
        }
    }
    private fun addToCart(){
        val cartItem=CartItem(
            FirestoreClass().getCurrentUserID(),
            mProductID,
            mProductDetails.title,
            mProductDetails.price,
            mProductDetails.image,
            Constants.DEFAULT_CART_QUANTITY
        )
        showProgressDialog("Adding this product to your bag...")
        FirestoreClass().addCartItems(this,cartItem)

    }
    fun addToCartSuccess(){
        hideProgressDialog()
        btn_add_to_cart.visibility=View.GONE
        btn_go_to_cart.visibility=View.VISIBLE
    }
    fun productExistsInCart(){
        hideProgressDialog()
        btn_add_to_cart.visibility=View.GONE
        btn_go_to_cart.visibility=View.VISIBLE
    }
    private fun defaultOptionsView(){
        val options=ArrayList<SMTextViewBold>()
        options.add(0,size_s)
        options.add(1,size_m)
        options.add(2,size_l)
        options.add(3,size_xl)

        for(option in options){
            option.background=ContextCompat.getDrawable(this,R.drawable.item_grey_border_background)
        }
    }
    private fun selectedOptionView(tv: SMTextViewBold, selectedOptionNum:Int){
        defaultOptionsView()
        tv.background= ContextCompat.getDrawable(this,R.drawable.item_size_background)

    }
    override fun onClick(v: View?) {
        if(v!=null){
            when(v.id){
                R.id.size_s->{
                    selectedOptionView(size_s,0)
                }
                R.id.size_m->{
                    selectedOptionView(size_m,1)
                }
                R.id.size_l->{
                    selectedOptionView(size_l,2)
                }
                R.id.size_xl->{
                    selectedOptionView(size_xl,3)
                }

                R.id.btn_add_to_cart->{
                    addToCart()
                }
                R.id.btn_go_to_cart->{
                    val intent=Intent(this,CartListActivity::class.java)
                    startActivity(intent)
                }
            }
        }

    }


}