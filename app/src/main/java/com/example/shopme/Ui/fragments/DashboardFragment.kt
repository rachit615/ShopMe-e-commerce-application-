package com.example.shopme.Ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopme.Firestore.FirestoreClass

import com.example.shopme.R
import com.example.shopme.Ui.activities.CartListActivity
import com.example.shopme.Ui.activities.SettingsActivity
import com.example.shopme.Ui.adapters.DashboardItemListAdapter
import com.example.shopme.Ui.adapters.MyProductsListAdapter
import com.example.shopme.models.Product
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_products.*


class DashboardFragment : BaseFragment() {

//    private lateinit var homeViewModel: HomeViewModel
//   this method will be call() when this fragment is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    getProductsListFromFirestore()
    }


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
//        homeViewModel =
//                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        return root
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        when(id){
            R.id.action_settings->{
                val intent=Intent(activity,SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_Shopping_bag->{
                val intent=Intent(activity,CartListActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun getProductsListFromFirestore(){
        showProgressDialog("Loading products...")
        FirestoreClass().getDashboardItemsList(this)
    }
    fun successProductsListFromFirestore(productsList:ArrayList<Product>){
        hideProgressDialog()
        if(productsList.size>0){
            rv_my_dashboard_items.visibility=View.VISIBLE
            tv_no_dashboard_items_found.visibility=View.GONE

            rv_my_dashboard_items.layoutManager= GridLayoutManager(activity,2)
            rv_my_dashboard_items.setHasFixedSize(true)
            val adapter= DashboardItemListAdapter(requireActivity(),productsList)
            rv_my_dashboard_items.adapter=adapter
        }
        else{
            rv_my_dashboard_items.visibility=View.GONE
        }
    }

}