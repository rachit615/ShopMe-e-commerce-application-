package com.example.shopme.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id:String="",
    val firstname:String="",
    val lastname:String="",
    val email:String="",
    val mobile:Long=0,
    val image:String="",
    val gender:String="",
    val profileCompleted:Int=0
):Parcelable

