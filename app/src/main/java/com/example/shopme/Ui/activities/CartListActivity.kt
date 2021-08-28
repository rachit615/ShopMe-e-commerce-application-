package com.example.shopme.Ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopme.Firestore.FirestoreClass
import com.example.shopme.R
import com.example.shopme.Ui.adapters.CartItemsListAdapter
import com.example.shopme.models.CartItem
import com.example.shopme.models.Product
import com.example.shopme.utils.Constants
import kotlinx.android.synthetic.main.activity_cart_list.*
import kotlinx.android.synthetic.main.activity_product_details.*

class CartListActivity : BaseActivity() {
    private lateinit var mProductsList:ArrayList<Product>
    private lateinit var mCartList:ArrayList<CartItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)
        setupActionBar()
        btn_shop_now.setOnClickListener {
            val intent=Intent(this@CartListActivity,DashboardActivity::class.java)
            startActivity(intent)
        }
        btn_checkout.setOnClickListener{
            val intent=Intent(this@CartListActivity,AddressListActivity::class.java)
            intent.putExtra(Constants.EXTRA_SELECT_ADDRESS,true)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        getAllProductsListItems()
//
    }
    private fun getAllProductsListItems(){
        showProgressDialog("Please wait...")
        FirestoreClass().getAllProductsList(this@CartListActivity)
    }
    private fun getCartListItems(){
        FirestoreClass().getCartList(this@CartListActivity)
    }
    fun itemUpdateSuccess(){
        hideProgressDialog()
        getCartListItems()
    }
    fun successProductsListFromFirestore(productsList:ArrayList<Product>){
        hideProgressDialog()
        mProductsList=productsList
        getCartListItems()
    }
    fun successCartItemList(cartList:ArrayList<CartItem>){
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
        mCartList=cartList


        if (mCartList.size>0){
            rv_cart_items_list.visibility=View.VISIBLE
            ll_checkout.visibility=View.VISIBLE
            iv_nothing_in_bag.visibility=View.GONE
            tv_no_cart_item_found.visibility= View.GONE
            hey_it_feels_so_light.visibility=View.GONE
            btn_shop_now.visibility=View.GONE
            rv_cart_items_list.layoutManager=LinearLayoutManager(this@CartListActivity)
            rv_cart_items_list.setHasFixedSize(true)
            val cartListAdapter=CartItemsListAdapter(this@CartListActivity,mCartList,true)
            rv_cart_items_list.adapter=cartListAdapter

            var subTotal:Double=0.0
            for (item in mCartList){
                val availableQuantity=item.stock_quantity.toInt()
                if(availableQuantity>0){
                    val price=item.price.toDouble()
                    val quantity=item.cart_quantity.toInt()
                    subTotal+=(price*quantity)
                }

            }
            tv_sub_total.text="₹${subTotal}"
            tv_shipping_charge.text="₹ 40"
            if(subTotal>0){
                ll_checkout.visibility=View.VISIBLE
                val total=subTotal+40
                tv_total_amount.text="₹${total}"
            }else{
                ll_checkout.visibility=View.GONE
            }
        }

        else{
            rv_cart_items_list.visibility=View.GONE
            ll_checkout.visibility=View.GONE
            iv_nothing_in_bag.visibility=View.VISIBLE
            hey_it_feels_so_light.visibility=View.VISIBLE
            btn_shop_now.visibility=View.VISIBLE
            tv_no_cart_item_found.visibility=View.VISIBLE

        }
    }
    fun itemRemovedSuccess() {
        hideProgressDialog()
        Toast.makeText(this,"Item has been removed successfully",Toast.LENGTH_SHORT).show()
        getCartListItems()
    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_cart_list_activity)
        val actionBar=supportActionBar
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_back_keyboard)
        }
        toolbar_cart_list_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }
}