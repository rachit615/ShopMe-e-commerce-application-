package com.example.shopme.Ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.shopme.Firestore.FirestoreClass
import com.example.shopme.R
import com.example.shopme.Ui.activities.CartListActivity
import com.example.shopme.models.CartItem
import com.example.shopme.models.Product
import com.example.shopme.utils.Constants
import com.example.shopme.utils.GlideLoader
import kotlinx.android.synthetic.main.item_cart_layout.view.*

open class CartItemsListAdapter (
    private val context: Context,
    private var list:ArrayList<CartItem>,
    private val updateCartItems:Boolean
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CartItemsListAdapter.MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_cart_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model=list[position]
        if(holder is MyViewHolder) {
            GlideLoader(context).loadProductPicture(model.image, holder.itemView.iv_cart_item_image)

            holder.itemView.tv_cart_item_title.text = model.title
            holder.itemView.tv_cart_item_price.text = "₹${model.price}"
            holder.itemView.tv_cart_quantity.text = model.cart_quantity

            if(model.cart_quantity=="0"){
                holder.itemView.ib_remove_cart_item.visibility=View.GONE
                holder.itemView.ib_add_cart_item.visibility=View.GONE
                holder.itemView.tv_cart_quantity.text="OUT OF STOCK"
                holder.itemView.tv_cart_quantity.setTextColor(ContextCompat.getColor(context,R.color.snackbarError))
                if (updateCartItems) {
                    holder.itemView.ib_delete_cart_item.visibility = View.VISIBLE
                } else {
                    holder.itemView.ib_delete_cart_item.visibility = View.GONE
                }
            }else {
                if (updateCartItems) {
                    holder.itemView.ib_remove_cart_item.visibility = View.VISIBLE
                    holder.itemView.ib_add_cart_item.visibility = View.VISIBLE
                    holder.itemView.ib_delete_cart_item.visibility = View.VISIBLE
                } else {

                    holder.itemView.ib_remove_cart_item.visibility = View.GONE
                    holder.itemView.ib_add_cart_item.visibility = View.GONE
                    holder.itemView.ib_delete_cart_item.visibility = View.GONE
                }
                holder.itemView.tv_cart_quantity.setTextColor(
                        ContextCompat.getColor(
                                context,
                                R.color.colorSecondaryText
                        )
                )
            }
            holder.itemView.ib_delete_cart_item.setOnClickListener {
                when(context){
                    is CartListActivity->{
                        context.showProgressDialog("Please wait...")
                    }
                }
                FirestoreClass().removeItemFromCart(context,model.id)
            }

            holder.itemView.ib_remove_cart_item.setOnClickListener {
                if(model.cart_quantity=="1"){
                    FirestoreClass().removeItemFromCart(context,model.id)
                }else{
                    val cartQuantity:Int=model.cart_quantity.toInt()
                    val itemHashMap=HashMap<String,Any>()
                    itemHashMap[Constants.CART_QUANTITY]=(cartQuantity-1).toString()
                    if(context is CartListActivity){
                        context.showProgressDialog("Please wait...")
                    }
                    FirestoreClass().updateMyCart(context,model.id,itemHashMap)
                }
            }

            holder.itemView.ib_add_cart_item.setOnClickListener {
                if(model.cart_quantity==model.stock_quantity){
                    Toast.makeText(context,"We're Sorry! Only ${model.stock_quantity} units allowed in each stock.",Toast.LENGTH_SHORT).show()
                }else{
                    val cartQuantity=model.cart_quantity.toInt()
                    val itemHashMap=HashMap<String,Any>()
                    itemHashMap[Constants.CART_QUANTITY]=(cartQuantity+1).toString()
                    if (context is CartListActivity){
                        context.showProgressDialog("Please wait...")
                    }
                    FirestoreClass().updateMyCart(context,model.id,itemHashMap)
                }

            }
        }

    }

    private class MyViewHolder(view: View):RecyclerView.ViewHolder(view)

}