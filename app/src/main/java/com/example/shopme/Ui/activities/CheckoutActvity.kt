package com.example.shopme.Ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopme.Firestore.FirestoreClass
import com.example.shopme.R
import com.example.shopme.Ui.adapters.CartItemsListAdapter
import com.example.shopme.models.Address
import com.example.shopme.models.CartItem
import com.example.shopme.models.Order
import com.example.shopme.models.Product
import com.example.shopme.utils.Constants
import kotlinx.android.synthetic.main.activity_cart_list.*
import kotlinx.android.synthetic.main.activity_checkout_actvity.*
import kotlinx.android.synthetic.main.activity_settings.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CheckoutActvity : BaseActivity() {
    private var mAddressDetails:Address?=null
    private lateinit var mProductsList: ArrayList<Product>
    private lateinit var mCartItemList:ArrayList<CartItem>
    private var mSubTotal: Double = 0.0
    private var mTotalAmount: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout_actvity)
        if(intent.hasExtra(Constants.EXTRA_SELECTED_ADDRESS)){
            mAddressDetails=intent.getParcelableExtra<Address>(Constants.EXTRA_SELECTED_ADDRESS)
        }
        if(mAddressDetails!=null){
            tv_checkout_address_type.text="${mAddressDetails!!.type}"
            tv_checkout_full_name.text="${mAddressDetails!!.name}"
            tv_checkout_address.text="${mAddressDetails!!.address}"
            tv_checkout_additional_note.text="${mAddressDetails!!.additionalNote}"
            tv_checkout_mobile_number.text="${mAddressDetails!!.mobileNumber}"
        }
        getProductsList()
        btn_place_order.setOnClickListener{
            placeAnOrder()
        }
    }
    private fun placeAnOrder(){
        showProgressDialog("Please wait...")

        if(mAddressDetails!=null){
            val order=Order(
                FirestoreClass().getCurrentUserID(),
                mCartItemList,
                mAddressDetails!!,
                "Order ID: ${mCartItemList[0].title}",
                mCartItemList[0].image,
                mSubTotal.toString(),
                "₹40",
                mTotalAmount.toString()
            )
            FirestoreClass().placeOrder(this@CheckoutActvity,order)
        }
    }
    fun allDetailsUpdatedSuccessfully(){
        hideProgressDialog()
        Toast.makeText(this,"Your Order has been placed Successfully.",Toast.LENGTH_SHORT).show()
        val intent = Intent(this@CheckoutActvity, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
    fun orderPlacedSuccess(){
        FirestoreClass().updateAllDetails(this@CheckoutActvity,mCartItemList)

    }
    private fun getProductsList(){
        showProgressDialog("Please wait...")
        FirestoreClass().getAllProductsList(this@CheckoutActvity)
    }
    fun successProductsListFromFirestore(productsList:ArrayList<Product>){
        mProductsList=productsList
        getCartItemsList()
    }
    private fun getCartItemsList(){
        FirestoreClass().getCartList(this)
    }
    fun successCartListFromFirestore(cartList:ArrayList<CartItem>){
        hideProgressDialog()

        for(product in mProductsList){
            for(cartItem in cartList){
                if(product.product_id==cartItem.product_id){
                    cartItem.stock_quantity=product.stock_quantity
                    if(product.stock_quantity.toInt()==0){
                        cartItem.stock_quantity=product.stock_quantity
                    }
                }
            }
        }

        mCartItemList=cartList
        rv_cart_list_items.layoutManager=LinearLayoutManager(this@CheckoutActvity)
        rv_cart_list_items.setHasFixedSize(true)

        val cartListAdapter=CartItemsListAdapter(this,mCartItemList,false)
        rv_cart_list_items.adapter=cartListAdapter

        for(cartItem in mCartItemList){
            val availableQuantity=cartItem.stock_quantity.toInt()
            if(availableQuantity>0){
                val price=cartItem.price.toDouble()
                val quantity=cartItem.cart_quantity.toInt()
                mSubTotal+=(price*quantity)
            }
        }
        tv_checkout_sub_total.text="${mSubTotal}"
        tv_checkout_shipping_charge.text="₹40"
        if (mSubTotal>0){
            ll_checkout_place_order.visibility=View.VISIBLE
            mTotalAmount=mSubTotal+40
            tv_checkout_total_amount.text="₹${mTotalAmount}"
        }else{
            ll_checkout_place_order.visibility=View.GONE
        }
    }
}