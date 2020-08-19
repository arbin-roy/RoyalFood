package com.arbin.fastfood.fragments

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission

import com.arbin.fastfood.R
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream


class MyProfileFragment : Fragment() {

    private lateinit var txtActualName: TextView
    private lateinit var txtActualEmail: TextView
    private lateinit var txtActualMobile: TextView
    private lateinit var txtActualAddress: TextView
    //private lateinit var imgProfileFragment: ImageView
    private lateinit var profile_image : CircleImageView
    private lateinit var userName: SharedPreferences
    private lateinit var userEmail: SharedPreferences
    private lateinit var userAddress: SharedPreferences
    private lateinit var userMobile: SharedPreferences
    private lateinit var userImage: SharedPreferences
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
        //imgProfileFragment = view.findViewById(R.id.imgProfileFragment)
        profile_image = view.findViewById(R.id.imgProfileFragment)

        userName= activity?.getSharedPreferences(getString(R.string.user_data_name), Context.MODE_PRIVATE) ?: return null
        userEmail= activity?.getSharedPreferences(getString(R.string.user_data_email), Context.MODE_PRIVATE) ?: return null
        userAddress= activity?.getSharedPreferences(getString(R.string.user_data_address), Context.MODE_PRIVATE) ?: return null
        userMobile= activity?.getSharedPreferences(getString(R.string.user_data_mobile), Context.MODE_PRIVATE) ?: return null
        userImage = activity?.getSharedPreferences(getString(R.string.user_image), Context.MODE_PRIVATE) ?: return null

        registeredUserName= userName.getString("Name", "Name")
        registeredUserMobile= userMobile.getString("Mobile", "Mobile")
        registeredUserEmail= userEmail.getString("Email", "Email")
        registeredUserAddress= userAddress.getString("Address", "Address")

        txtActualName.text= registeredUserName
        txtActualEmail.text= registeredUserEmail
        txtActualMobile.text= registeredUserMobile
        txtActualAddress.text= registeredUserAddress

        /*profile_image.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(activity as Context , Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permission, PERMISSION_CODE)
                }else{
                    pickFromGallery()
                }
            }else{
                pickFromGallery()
            }
        }*/

        return view
    }

    private fun pickFromGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickFromGallery()
                }else{
                    Toast.makeText(activity, "Permission Denied", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            val bm = BitmapFactory.decodeFile((data?.data).toString())
            profile_image.setImageBitmap(bm)
            /*val baos = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val b = baos.toByteArray()
            val stringImage = Base64.encodeToString(b, Base64.DEFAULT)
            userImage.edit().putString("image", stringImage).apply()
            val encodedString= userImage.getString("image", null)
            val byte = Base64.decode(encodedString, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(byte, 0, byte.size)
            //val uri = Uri.parse(userImage.getString("image", null))
            profile_image.setImageBitmap(bitmap)*/
        }
    }

    companion object{
        private val IMAGE_PICK_CODE = 1000
        private val PERMISSION_CODE = 1001
    }
}

