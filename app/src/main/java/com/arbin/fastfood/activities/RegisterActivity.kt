package com.arbin.fastfood.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.arbin.fastfood.R
import org.json.JSONException
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    private lateinit var etRegName: EditText
    private lateinit var etRegEmail: EditText
    private lateinit var etRegMobile: EditText
    private lateinit var etRegAddress: EditText
    private lateinit var etRegPassword: EditText
    private lateinit var etRegConfirmPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var toolbarRegActivity: Toolbar
    private lateinit var sendName: String
    private lateinit var sendMobileNumber: String
    private lateinit var sendEmail: String
    private lateinit var sendAddress: String
    private lateinit var sendPassword: String
    lateinit var userId: SharedPreferences
    lateinit var userName: SharedPreferences
    lateinit var userEmail: SharedPreferences
    lateinit var userAddress: SharedPreferences
    lateinit var userMobile: SharedPreferences
    lateinit var sharedPreferences: SharedPreferences
    lateinit var registeredUserID: String
    lateinit var registeredUserName: String
    lateinit var registeredUserMobile: String
    lateinit var registeredUserEmail: String
    lateinit var registeredUserAddress: String
    lateinit var rlRegisterActivity: RelativeLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etRegName= findViewById(R.id.etRegName)
        etRegEmail= findViewById(R.id.etRegEmail)
        etRegMobile= findViewById(R.id.etRegMobile)
        etRegAddress= findViewById(R.id.etRegAddress)
        etRegPassword= findViewById(R.id.etRegPassword)
        etRegConfirmPassword= findViewById(R.id.etRegConfirmPassword)
        btnRegister= findViewById(R.id.btnRegister)
        toolbarRegActivity= findViewById(R.id.toolbarRegActivity)
        rlRegisterActivity= findViewById(R.id.rlRegisterActivity)
        rlRegisterActivity.visibility= View.GONE

        userId= getSharedPreferences(getString(R.string.user_id), Context.MODE_PRIVATE)
        userName= getSharedPreferences(getString(R.string.user_data_name), Context.MODE_PRIVATE)
        userEmail= getSharedPreferences(getString(R.string.user_data_email), Context.MODE_PRIVATE)
        userAddress= getSharedPreferences(getString(R.string.user_data_address), Context.MODE_PRIVATE)
        userMobile= getSharedPreferences(getString(R.string.user_data_mobile), Context.MODE_PRIVATE)
        sharedPreferences= getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        setUpToolbar()

        btnRegister.setOnClickListener {
            if (etRegPassword.text.toString() == etRegConfirmPassword.text.toString() || etRegPassword.text.length<4){
                rlRegisterActivity.visibility = View.VISIBLE

                val url ="http://13.235.250.119/v2/register/fetch_result"
                val queue= Volley.newRequestQueue(this@RegisterActivity)

                sendName= etRegName.text.toString()
                sendEmail= etRegEmail.text.toString()
                sendMobileNumber= etRegMobile.text.toString()
                sendPassword= etRegPassword.text.toString()
                sendAddress= etRegAddress.text.toString()

                val jsonParams= JSONObject()
                jsonParams.put("name", sendName)
                jsonParams.put("mobile_number", sendMobileNumber)
                jsonParams.put("password", sendPassword)
                jsonParams.put("address", sendAddress)
                jsonParams.put("email", sendEmail)

                val jsonRequest= object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {
                    try {
                        if (it.getJSONObject("data").getBoolean("success")){
                            Toast.makeText(this@RegisterActivity, "Successfully Registered", Toast.LENGTH_LONG).show()
                            val data= it.getJSONObject("data").getJSONObject("data")
                            registeredUserID = data.getString("user_id")
                            registeredUserName = data.getString("name")
                            registeredUserMobile = data.getString("mobile_number")
                            registeredUserEmail = data.getString("email")
                            registeredUserAddress = data.getString("address")

                            userId.edit().putString("ID", registeredUserID).apply()
                            userName.edit().putString("Name", registeredUserName).apply()
                            userMobile.edit().putString("Mobile", registeredUserMobile).apply()
                            userEmail.edit().putString("Email", registeredUserEmail).apply()
                            userAddress.edit().putString("Address", registeredUserAddress).apply()
                            sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()

                            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()

                        }else{
                            rlRegisterActivity.visibility = View.GONE
                            Toast.makeText(this@RegisterActivity, it.getJSONObject("data").getString("errorMessage"), Toast.LENGTH_LONG).show()
                        }
                    }catch (e: JSONException){
                        Toast.makeText(this@RegisterActivity, "JsonException $it", Toast.LENGTH_LONG).show()
                    }
                }, Response.ErrorListener {
                    val dialog = AlertDialog.Builder(this@RegisterActivity)
                    dialog.setTitle("Error")
                    dialog.setMessage("Connection Established failed")
                    dialog.setPositiveButton("Wait"){ _, _ ->
                        rlRegisterActivity.visibility = View.GONE
                    }
                    dialog.setNegativeButton("Exit") { _, _ ->
                        finishAffinity()
                    }
                    dialog.create()
                    dialog.show()
                }){
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers= HashMap<String, String>()
                        headers["Content-type"]= "application/json"
                        headers["token"]= "aebb3b4a51733f"
                        return headers
                    }
                }
                queue.add(jsonRequest)
            }else{
                rlRegisterActivity.visibility = View.GONE
                Toast.makeText(this, "Invalid Password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUpToolbar(){
        setSupportActionBar(toolbarRegActivity)
        supportActionBar?.title= "Register Yourself"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }
}
