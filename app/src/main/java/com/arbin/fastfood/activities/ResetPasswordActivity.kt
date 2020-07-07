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
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.arbin.fastfood.R
import org.json.JSONException
import org.json.JSONObject

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var resetPassOTP: EditText
    private lateinit var resetPassNew: EditText
    private lateinit var resetPassConfirm: EditText
    lateinit var btnSubmit: Button
    private lateinit var otp: String
    lateinit var newPassword: String
    lateinit var confirmPassword: String
    lateinit var sharedPreferences: SharedPreferences
    lateinit var rlResetPassword: RelativeLayout

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        resetPassOTP= findViewById(R.id.resetPassOTP)
        resetPassNew= findViewById(R.id.resetPassNew)
        resetPassConfirm= findViewById(R.id.resetPassConfirm)
        btnSubmit= findViewById(R.id.btnSubmit)
        rlResetPassword= findViewById(R.id.rlResetPassword)
        rlResetPassword.visibility= View.GONE
        sharedPreferences= getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        btnSubmit.setOnClickListener {
            rlResetPassword.visibility= View.VISIBLE
            btnSubmit.visibility= View.GONE

            otp= resetPassOTP.text.toString()
            newPassword= resetPassNew.text.toString()
            confirmPassword= resetPassConfirm.text.toString()
            val number= intent.getStringExtra("numBer")

            val queue= Volley.newRequestQueue(this@ResetPasswordActivity)
            val url= "http://13.235.250.119/v2/reset_password/fetch_result"

            val jsonParams= JSONObject()
            jsonParams.put("mobile_number", number.toString())
            jsonParams.put("password", confirmPassword)
            jsonParams.put("otp", otp)

            val jsonRequest= object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {
                try {
                    if (newPassword == confirmPassword){
                        if (it.getJSONObject("data").getBoolean("success")){
                            Toast.makeText(this@ResetPasswordActivity, it.getJSONObject("data").getString("successMessage"), Toast.LENGTH_LONG).show()
                            sharedPreferences.edit().clear().apply()
                            val intent= Intent(this@ResetPasswordActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else{
                            rlResetPassword.visibility= View.GONE
                            btnSubmit.visibility= View.VISIBLE
                            Toast.makeText(this@ResetPasswordActivity, it.getJSONObject("data").getString("errorMessage"), Toast.LENGTH_LONG).show()
                        }
                    }else{
                        rlResetPassword.visibility= View.GONE
                        btnSubmit.visibility= View.VISIBLE
                        Toast.makeText(this@ResetPasswordActivity, "Check Passwords field again", Toast.LENGTH_SHORT).show()
                    }
                }catch (e: JSONException){
                    Toast.makeText(this@ResetPasswordActivity, "JSONException", Toast.LENGTH_SHORT).show()
                }
            }, Response.ErrorListener {
                val dialog = AlertDialog.Builder(this@ResetPasswordActivity)
                dialog.setTitle("Error")
                dialog.setMessage("Connection Established failed")
                dialog.setPositiveButton("Wait"){ _ , _ ->
                    rlResetPassword.visibility= View.GONE
                    btnSubmit.visibility= View.VISIBLE
                }
                dialog.setNegativeButton("Exit") { _ , _ ->
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
        }
    }

    override fun onBackPressed() {
        val dialog= AlertDialog.Builder(this@ResetPasswordActivity)
        dialog.setTitle("Confirmation")
        dialog.setMessage("Do you want to cancel the process? Your password will not change")
        dialog.setPositiveButton("Go Back"){ _ , _ ->
            startActivity(Intent(this@ResetPasswordActivity, LoginActivity::class.java))
            finish()
        }
        dialog.setNegativeButton("Cancel"){ _ , _ ->

        }
        dialog.create()
        dialog.show()
    }
}
