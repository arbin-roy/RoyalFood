package com.arbin.fastfood.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.arbin.fastfood.R

class SplashScreen : AppCompatActivity() {

    private lateinit var loggedInStatus: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        loggedInStatus= getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        val isLoggedIn= loggedInStatus.getBoolean("isLoggedIn", false)

        if (isLoggedIn){
            Handler().postDelayed({startActivity(Intent(this@SplashScreen, MainActivity::class.java))
            finish()
            }, 2000)
        }else{
            Handler().postDelayed(
                {startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
                    finish()
                }, 2000)
        }
    }
}
