package com.arbin.fastfood.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.arbin.fastfood.R


class MyProfileFragment : Fragment() {

    private lateinit var txtActualName: TextView
    private lateinit var txtActualEmail: TextView
    private lateinit var txtActualMobile: TextView
    private lateinit var txtActualAddress: TextView
    private lateinit var userName: SharedPreferences
    private lateinit var userEmail: SharedPreferences
    private lateinit var userAddress: SharedPreferences
    private lateinit var userMobile: SharedPreferences
    private var registeredUserName: String? = "Your Name"
    private var registeredUserMobile: String?= "Your Mobile"
    private var registeredUserEmail: String?= "Your Email"
    private var registeredUserAddress: String?= "Your Address"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_my_profile, container, false)

        txtActualName= view.findViewById(R.id.txtActualName)
        txtActualEmail= view.findViewById(R.id.txtActualEmail)
        txtActualMobile= view.findViewById(R.id.txtActualMobile)
        txtActualAddress= view.findViewById(R.id.txtActualAddress)

        userName= activity?.getSharedPreferences(getString(R.string.user_data_name), Context.MODE_PRIVATE) ?: return null
        userEmail= activity?.getSharedPreferences(getString(R.string.user_data_email), Context.MODE_PRIVATE) ?: return null
        userAddress= activity?.getSharedPreferences(getString(R.string.user_data_address), Context.MODE_PRIVATE) ?: return null
        userMobile= activity?.getSharedPreferences(getString(R.string.user_data_mobile), Context.MODE_PRIVATE) ?: return null

        registeredUserName= userName.getString("Name", "Name")
        registeredUserMobile= userMobile.getString("Mobile", "Mobile")
        registeredUserEmail= userEmail.getString("Email", "Email")
        registeredUserAddress= userAddress.getString("Address", "Address")

        txtActualName.text= registeredUserName
        txtActualEmail.text= registeredUserEmail
        txtActualMobile.text= registeredUserMobile
        txtActualAddress.text= registeredUserAddress

        return view
    }
}
