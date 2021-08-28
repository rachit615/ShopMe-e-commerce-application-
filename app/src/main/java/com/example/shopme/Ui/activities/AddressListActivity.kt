package com.example.shopme.Ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopme.Firestore.FirestoreClass
import com.example.shopme.R
import com.example.shopme.Ui.adapters.AddressListAdapter
import com.example.shopme.models.Address
import com.example.shopme.utils.Constants
import kotlinx.android.synthetic.main.activity_address_list.*
import kotlinx.android.synthetic.main.activity_settings.*

class AddressListActivity : BaseActivity() {
    private var mSelectedAddress:Boolean=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)
        setupActionBar()

        tv_add_address.setOnClickListener {
            val intent = Intent(this@AddressListActivity, AddEditAddressActivity::class.java)
            startActivity(intent)
        }

        if(intent.hasExtra(Constants.EXTRA_SELECT_ADDRESS)){
            mSelectedAddress=intent.getBooleanExtra(Constants.EXTRA_SELECT_ADDRESS,false)
        }
        if(mSelectedAddress){
            tv_title_address.text="SELECT ADDRESS"
        }else{
            tv_title_address.text="ADDRESS"
        }

    }

    override fun onResume() {
        super.onResume()
        getAddressList()
    }
    fun setupActionBar(){
        setSupportActionBar(toolbar_address_list_activity)
        val actionBar=supportActionBar
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_back_ios)
        }
        toolbar_address_list_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }
    private fun getAddressList(){
        showProgressDialog("Loading your Addresses...")
        FirestoreClass().getAddressesList(this@AddressListActivity)
    }
    fun successAddressListFromFirestore(addressList: ArrayList<Address>){
        hideProgressDialog()
        if(addressList.size>0){
            rv_address_list.visibility=View.VISIBLE
            tv_no_address_found.visibility=View.GONE

            rv_address_list.layoutManager=LinearLayoutManager(this@AddressListActivity)
            rv_address_list.setHasFixedSize(true)
            val addressAdapter=AddressListAdapter(this@AddressListActivity,addressList,mSelectedAddress)
            rv_address_list.adapter=addressAdapter
        }else{
            rv_address_list.visibility=View.GONE
            tv_no_address_found.visibility=View.VISIBLE
        }
    }
}