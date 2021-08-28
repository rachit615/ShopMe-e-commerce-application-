package com.example.shopme.Ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopme.Firestore.FirestoreClass

import com.example.shopme.R
import com.example.shopme.Ui.activities.AddProductActivity
import com.example.shopme.Ui.activities.SettingsActivity
import com.example.shopme.Ui.adapters.MyProductsListAdapter
import com.example.shopme.models.Product
import kotlinx.android.synthetic.main.fragment_products.*


class ProductsFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        getProductListFromFirestore()
        btn_add_now.setOnClickListener{
            val intent=Intent(activity,AddProductActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
//
        val root = inflater.inflate(R.layout.fragment_products, container, false)
        return root
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_product_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        when(id){
            R.id.action_add_product->{
                val intent= Intent(activity, AddProductActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun getProductListFromFirestore(){
        showProgressDialog("Loading your products...")
        FirestoreClass().getProductsList(this)
    }
    fun deleteProduct(productID:String){
        showAlertDialogToDeleteProduct(productID)

    }
    fun successOnProductDelete(){
        hideProgressDialog()
        Toast.makeText(context,"Succesfully Removed from Products list ✓",Toast.LENGTH_LONG).show()
        getProductListFromFirestore()
    }
    fun successProductsListFromFirestore(productsList: ArrayList<Product>){
        hideProgressDialog()
        if(productsList.size>0){
            rv_my_product_items.visibility=View.VISIBLE
            tv_no_products_found.visibility=View.GONE
            iv_two_shopping_bag.visibility=View.GONE
            btn_add_now.visibility=View.GONE

            rv_my_product_items.layoutManager=LinearLayoutManager(activity)
            rv_my_product_items.setHasFixedSize(true)
            val adapterProducts=MyProductsListAdapter(requireActivity(),productsList,this)
            rv_my_product_items.adapter=adapterProducts
        }
        else{
            rv_my_product_items.visibility=View.GONE
            tv_no_products_found.visibility=View.VISIBLE
            iv_two_shopping_bag.visibility=View.VISIBLE
            btn_add_now.visibility=View.VISIBLE
        }
    }
    private fun showAlertDialogToDeleteProduct(productID: String){
        val builder=AlertDialog.Builder(requireActivity())
        builder.setTitle("Remove from your Products")
        builder.setMessage("Are you sure you want to remove this item from your products?")
        builder.setPositiveButton("Yes"){ dialogInterface, _ ->
            FirestoreClass().deleteProductsFromFirestore(this,productID)
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("No"){ dialogInterface,_ ->
            dialogInterface.dismiss()
        }
        val alertDialog=builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()


    }

}