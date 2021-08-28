package com.example.shopme.Ui.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.shopme.R
import com.example.shopme.Ui.activities.CheckoutActvity
import com.example.shopme.models.Address
import com.example.shopme.utils.Constants
import kotlinx.android.synthetic.main.address_list_layout.view.*

open class AddressListAdapter(
    private val context:Context,
    private var list: ArrayList<Address>,
    private val selectAddress:Boolean

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.address_list_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            holder.itemView.tv_address_full_name.text = model.name
            holder.itemView.tv_address_type.text = model.type
            holder.itemView.tv_address_details.text = "${model.address}, ${model.zipCode}"
            holder.itemView.tv_address_mobile_number.text = model.mobileNumber
            if (selectAddress){
                holder.itemView.setOnClickListener{
                    holder.itemView.setBackgroundColor(Color.parseColor("#ED2173"))
                    Toast.makeText(
                        context,
                        "Selected address : ${model.address}, ${model.zipCode}",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent=Intent(context,CheckoutActvity::class.java)
                    intent.putExtra(Constants.EXTRA_SELECTED_ADDRESS,model)
                    context.startActivity(intent)
                }
            }
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }
//    fun notifyEditItem(activity: Activity, position: Int) {
//        val intent = Intent(context, AddEditAddressActivity::class.java)
//        intent.putExtra(Constants.EXTRA_ADDRESS_DETAILS, list[position])
//
//        // TODO Step 15: Make it startActivityForResult instead of startActivity.
//        // START
//        // activity.startActivity (intent)
//
//        activity.startActivityForResult(intent, Constants.ADD_ADDRESS_REQUEST_CODE)
//        // END
//
//        notifyItemChanged(position) // Notify any registered observers that the item at position has changed.
//    }
    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}