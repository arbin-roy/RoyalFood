package com.arbin.fastfood.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.arbin.fastfood.R
import org.json.JSONException
import org.json.JSONObject

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var btnNext: Button
    lateinit var txtForgotPasswordMobile: EditText
    lateinit var txtForgotPasswordEmail: EditText
    lateinit var rlForgotPassword: RelativeLayout
    lateinit var mobileNumber: String
    lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        btnNext= findViewById(R.id.btnNext)
        txtForgotPasswordMobile= findViewById(R.id.txtForgotPasswordMobile)
        txtForgotPasswordEmail= findViewById(R.id.txtForgotPasswordEmail)
        rlForgotPassword= findViewById(R.id.rlForgotPassword)
        rlForgotPassword.visibility= View.GONE

        btnNext.setOnClickListener {
            rlForgotPassword.visibility= View.VISIBLE
            btnNext.visibility= View.GONE
            mobileNumber= txtForgotPasswordMobile.text.toString()
            email= txtForgotPasswordEmail.text.toString()

            val url= "http://13.235.250.119/v2/forgot_password/fetch_result"
            val queue= Volley.newRequestQueue(this@ForgotPasswordActivity)

            val jsonParams= JSONObject()
            jsonParams.put("mobile_number", mobileNumber)
            jsonParams.put("email", email)

            val jsonRequest = object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {
                try {
                    if (it.getJSONObject("data").getBoolean("success")){
                        val otp= it.getJSONObject("data").getBoolean("first_try")
                        if (otp){
                            Toast.makeText(this@ForgotPasswordActivity, "OTP Send to the Mail-Address", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this@ForgotPasswordActivity, "OTP already send to the Mail-Address", Toast.LENGTH_SHORT).show()
                        }
                        val intent= Intent(this@ForgotPasswordActivity, ResetPasswordActivity::class.java)
                        intent.putExtra("numBer", mobileNumber)
                        startActivity(intent)
                        finish()
                    }else{
                        rlForgotPassword.visibility= View.GONE
                        btnNext.visibility= View.VISIBLE
                        Toast.makeText(this@ForgotPasswordActivity, it.getJSONObject("data").getString("errorMessage"), Toast.LENGTH_LONG).show()
                    }
                }catch (e: JSONException){
                    Toast.makeText(this@ForgotPasswordActivity, "Json Exception", Toast.LENGTH_LONG).show()
                }
            }, Response.ErrorListener {
                val dialog = AlertDialog.Builder(this@ForgotPasswordActivity)
                dialog.setTitle("Error")
                dialog.setMessage("Connection Established failed")
                dialog.setPositiveButton("Wait"){ _ , _ ->
                    rlForgotPassword.visibility= View.GONE
                    btnNext.visibility= View.VISIBLE
                }
                dialog.setNegativeButton("Exit") { _ , _ ->
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
        }
    }

    override fun onBackPressed() {
        val dialog= AlertDialog.Builder(this@ForgotPasswordActivity)
        dialog.setTitle("Cancel Process")
        dialog.setMessage("Do you want to cancel the process? Your password will not change")
        dialog.setPositiveButton("OK"){ _ , _ ->
            startActivity(Intent(this@ForgotPasswordActivity, LoginActivity::class.java))
            finish()
        }
        dialog.setNegativeButton("Cancel"){ _ , _ ->

        }
        dialog.create()
        dialog.show()
    }
}
