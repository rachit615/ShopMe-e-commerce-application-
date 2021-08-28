package com.example.shopme.Ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopme.Firestore.FirestoreClass
import com.example.shopme.R
import com.example.shopme.Ui.adapters.MyOrdersLIstAdapter
import com.example.shopme.models.Order
import kotlinx.android.synthetic.main.fragment_orders.*


class OrdersFragment : BaseFragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
//        notificationsViewModel =
//                ViewModelProvider(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_orders, container, false)
        return root
    }

    override fun onResume() {
        super.onResume()
        getMyOrdersList()
    }
    private fun getMyOrdersList(){
        showProgressDialog("Loading your Orders...")
        FirestoreClass().getMyOrdersList(this@OrdersFragment)
    }
    fun showOrdersListSuccess(ordersList:ArrayList<Order>){
        hideProgressDialog()

        if(ordersList.size>0){
            rv_my_order_items.visibility=View.VISIBLE
            tv_no_orders_found.visibility=View.GONE

            rv_my_order_items.layoutManager=LinearLayoutManager(activity)
            rv_my_order_items.setHasFixedSize(true)

            val adapterOrders=MyOrdersLIstAdapter(requireActivity(),ordersList,this@OrdersFragment)
            rv_my_order_items.adapter=adapterOrders
        }else{
            rv_my_order_items.visibility=View.GONE
            tv_no_orders_found.visibility=View.VISIBLE
        }
    }
}