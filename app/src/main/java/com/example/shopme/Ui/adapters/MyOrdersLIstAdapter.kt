package com.example.shopme.Ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopme.R
import com.example.shopme.Ui.fragments.OrdersFragment
import com.example.shopme.Ui.fragments.ProductsFragment
import com.example.shopme.models.Order
import com.example.shopme.utils.GlideLoader
import kotlinx.android.synthetic.main.item_list_layout.view.*


class MyOrdersLIstAdapter(
    private val context: Context,
    private var list:ArrayList<Order>,
    private val fragment: OrdersFragment
  ): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyOrdersLIstAdapter.MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_list_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model=list[position]
        if (holder is MyViewHolder){
            GlideLoader(context).loadProductPicture(model.image,holder.itemView.iv_item_image)
            holder.itemView.tv_item_name.text="${model.title}"
            holder.itemView.tv_item_price.text="₹${model.total_amount}"
            holder.itemView.ib_delete_product.visibility=View.GONE
        }
    }
    class MyViewHolder(view: View):RecyclerView.ViewHolder(view)

}

