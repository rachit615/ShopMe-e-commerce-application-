package com.example.shopme.Ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopme.R
import com.example.shopme.Ui.activities.ProductDetailsActivity
import com.example.shopme.models.Product
import com.example.shopme.utils.Constants
import com.example.shopme.utils.GlideLoader
import kotlinx.android.synthetic.main.item_dashboard_layout.view.*

open class DashboardItemListAdapter(
    private val context: Context,
    private var list:ArrayList<Product>
):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_dashboard_layout,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model=list[position]
        if (holder is MyViewHolder){
            GlideLoader(context).loadProductPicture(model.image,holder.itemView.iv_dashboard_item_image)
            holder.itemView.tv_dashboard_item_title.text=model.title
            holder.itemView.tv_dashboard_item_price.text="Rs. ${model.price}"

            holder.itemView.setOnClickListener{
                val intent= Intent(context, ProductDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_PRODUCT_ID,model.product_id)
                intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID,model.user_id)
                context.startActivity(intent)
            }
        }
    }
    class MyViewHolder(view: View):RecyclerView.ViewHolder(view)
}