package com.arbin.fastfood.activities

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.room.Room
import com.arbin.fastfood.R
import com.arbin.fastfood.database.CartItemDatabase
import com.arbin.fastfood.fragments.CartItemFragment

class CartActivity : AppCompatActivity() {

    private lateinit var toolbarCartActivity: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        toolbarCartActivity = findViewById(R.id.toolbarCartActivity)

        setUpToolbar()

        supportFragmentManager.beginTransaction().replace(R.id.CartFrameLayout, CartItemFragment(intent.getStringExtra("resName"))).commit()

    }

    private fun setUpToolbar(){
        setSupportActionBar(toolbarCartActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title= "My Cart"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }

    override fun onBackPressed() {
        when(supportFragmentManager.findFragmentById(R.id.CartFrameLayout)){
            !is CartItemFragment-> {
                ClearDatabase(this@CartActivity).execute()
                val intent = Intent(this@CartActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }else -> {
                super.onBackPressed()
                finish()
            }
        }
    }

    class ClearDatabase(val context: Context): AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean {
            val db = Room.databaseBuilder(context, CartItemDatabase::class.java, "cartlist-db").build()
            db.CartItemDao().delAllItem()
            db.close()
            return false
        }
    }
}
