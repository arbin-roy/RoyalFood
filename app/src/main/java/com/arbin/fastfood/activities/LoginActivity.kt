package com.arbin.fastfood.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.arbin.fastfood.R
import com.arbin.fastfood.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private lateinit var txtForgotPassword: TextView
    lateinit var btnLogin: Button
    private lateinit var txtRegister: TextView
    private lateinit var etMobileNumber: EditText
    private lateinit var etPassword: EditText
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var mobileNumber: String
    private lateinit var password: String
    lateinit var userId: SharedPreferences
    lateinit var userName: SharedPreferences
    lateinit var userEmail: SharedPreferences
    lateinit var userAddress: SharedPreferences
    lateinit var userMobile: SharedPreferences
    lateinit var registeredUserName: String
    lateinit var registeredUserMobile: String
    lateinit var registeredUserEmail: String
    lateinit var registeredUserAddress: String
    lateinit var registeredUserID: String
    lateinit var rlLoginActivity: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        txtForgotPassword= findViewById(R.id.txtForgotPassword)
        btnLogin= findViewById(R.id.btnLogin)
        txtRegister= findViewById(R.id.txtRegister)
        etMobileNumber= findViewById(R.id.etMobileNumber)
        etPassword= findViewById(R.id.etPassword)
        rlLoginActivity= findViewById(R.id.rlLoginActivity)
        rlLoginActivity.visibility= View.GONE

        sharedPreferences= getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        userId= getSharedPreferences(getString(R.string.user_id), Context.MODE_PRIVATE)
        userName= getSharedPreferences(getString(R.string.user_data_name), Context.MODE_PRIVATE)
        userEmail= getSharedPreferences(getString(R.string.user_data_email), Context.MODE_PRIVATE)
        userAddress= getSharedPreferences(getString(R.string.user_data_address), Context.MODE_PRIVATE)
        userMobile= getSharedPreferences(getString(R.string.user_data_mobile), Context.MODE_PRIVATE)

        txtForgotPassword.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
            finish()
        }
        btnLogin.setOnClickListener {
            rlLoginActivity.visibility= View.VISIBLE
            btnLogin.visibility= View.GONE
            mobileNumber = etMobileNumber.text.toString()
            password= etPassword.text.toString()
            val queue= Volley.newRequestQueue(this@LoginActivity)
            val url= "http://13.235.250.119/v2/login/fetch_result/"

            val jsonParams= JSONObject()
            jsonParams.put("mobile_number", mobileNumber)
            jsonParams.put("password", password)

            if (ConnectionManager().checkConnection(this@LoginActivity)){
                val jsonRequest= object: JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {
                    try {
                        if (it.getJSONObject("data").getBoolean("success")){
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
                            val intent= Intent(this@LoginActivity, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        }else{
                            rlLoginActivity.visibility= View.GONE
                            btnLogin.visibility= View.VISIBLE
                            Toast.makeText(this@LoginActivity, it.getJSONObject("data").getString("errorMessage"), Toast.LENGTH_LONG).show()
                        }
                    }catch (e :JSONException){
                        Toast.makeText(this@LoginActivity, "JSON Exception $it", Toast.LENGTH_LONG).show()
                    }
                }, Response.ErrorListener {
                    val dialog = AlertDialog.Builder(this@LoginActivity)
                    dialog.setTitle("Error")
                    dialog.setMessage("Connection Established failed")
                    dialog.setPositiveButton("Wait"){ _, _ ->
                        rlLoginActivity.visibility= View.GONE
                        btnLogin.visibility= View.VISIBLE
                    }
                    dialog.setNegativeButton("Exit") { _, _ ->
                        finishAffinity()
                    }
                    dialog.create()
                    dialog.show()
                }){
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers= HashMap<String, String>()
                        headers["Content-type"]="application/json"
                        headers["token"]="aebb3b4a51733f"
                        return headers
                    }
                }
                queue.add(jsonRequest)
            }else{
                val dialog = AlertDialog.Builder(this@LoginActivity)
                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection is not Found")
                dialog.setPositiveButton("Open Settings"){ _, _ ->
                    rlLoginActivity.visibility= View.GONE
                    btnLogin.visibility= View.VISIBLE
                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                }

                dialog.setNegativeButton("Exit") { _, _ ->
                    finishAffinity()
                }
                dialog.create()
                dialog.show()

            }
        }
        txtRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    override fun onBackPressed() {
        val dialog= AlertDialog.Builder(this@LoginActivity)
        dialog.setTitle("Confirmation")
        dialog.setMessage("Exit without Logging in?")
        dialog.setPositiveButton("Exit"){ _, _ ->
            finishAffinity()
        }
        dialog.setNegativeButton("Cancel"){ _, _ ->

        }
        dialog.create()
        dialog.show()
    }
}
