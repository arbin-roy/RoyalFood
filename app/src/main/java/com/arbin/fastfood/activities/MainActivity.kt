package com.arbin.fastfood.activities

import android.content.*
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.room.Room
import com.arbin.fastfood.R
import com.arbin.fastfood.database.CartItemDatabase
import com.arbin.fastfood.database.CartItemEntities
import com.arbin.fastfood.database.ResturantDatabase
import com.arbin.fastfood.fragments.*
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(){

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var coordinatorLayout: CoordinatorLayout
    private lateinit var toolbarMainActivity: androidx.appcompat.widget.Toolbar
    private lateinit var frameLayout: FrameLayout
    private lateinit var navigationView: NavigationView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userName: SharedPreferences
    private lateinit var userMobile: SharedPreferences
    private var previousMenuItem: MenuItem? = null
    private lateinit var foodIdList: List<CartItemEntities>
    private lateinit var drawerUserName: TextView
    private lateinit var drawerUserNumber: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout= findViewById(R.id.drawerLayout)
        coordinatorLayout= findViewById(R.id.coordinatorLayout)
        toolbarMainActivity= findViewById(R.id.toolbarMainActivity)
        frameLayout= findViewById(R.id.frameLayout)
        navigationView= findViewById(R.id.navigationView)
        sharedPreferences= getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        userName = getSharedPreferences(getString(R.string.user_data_name), Context.MODE_PRIVATE)
        userMobile = getSharedPreferences(getString(R.string.user_data_mobile), Context.MODE_PRIVATE)

        val headerView=  navigationView.getHeaderView(0)
        drawerUserName = headerView.findViewById(R.id.drawerUserName)
        drawerUserNumber = headerView.findViewById(R.id.drawerUserNumber)

        drawerUserName.text = userName.getString("Name", "Name")
        drawerUserNumber.text = userMobile.getString("Mobile", "Mobile")

        setUpToolbar()

        val actionBarDrawerToggle = ActionBarDrawerToggle(
                this@MainActivity,
                drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        openHome()

        navigationView.setNavigationItemSelectedListener {
            if (previousMenuItem != null){
                previousMenuItem?.isChecked= false
            }
            it.isCheckable= true
            it.isChecked= true
            previousMenuItem = it

            when(it.itemId){
                R.id.allResturant->{
                    openHome()
                    drawerLayout.closeDrawers()
                }

                R.id.profile->{
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, MyProfileFragment()).commit()
                    supportActionBar?.title = "My Profile"
                    drawerLayout.closeDrawers()
                }

                R.id.favorite->{
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, FavoriteResturantFragment()).commit()
                    supportActionBar?.title = "Favorite Restaurants"
                    drawerLayout.closeDrawers()
                }

                R.id.orderHistory->{
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, OrderHistoryFragment()).commit()
                    supportActionBar?.title = "Order History"
                    drawerLayout.closeDrawers()
                }

                R.id.faqs->{
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, FAQFragment()).commit()
                    supportActionBar?.title = "Frequently Asked Questions"
                    drawerLayout.closeDrawers()
                }

                R.id.aboutApp -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, AboutAppFragment()).commit()
                    supportActionBar?.title = "About App"
                    drawerLayout.closeDrawers()
                }

                R.id.logout->{
                    val dialog= AlertDialog.Builder(this@MainActivity)
                    dialog.setTitle("Logout")
                    dialog.setMessage("Are you sure you want to logout?")
                    dialog.setPositiveButton("Logout"){ _, _ ->
                        sharedPreferences.edit().clear().apply()
                        ClearDatabase(this@MainActivity).execute()
                        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                        finish()
                    }
                    dialog.setNegativeButton("Cancel"){ _, _ ->
                        openHome()
                    }
                    dialog.create()
                    dialog.show()
                    drawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }

        headerView.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.frameLayout, MyProfileFragment()).commit()
            supportActionBar?.title = "My Profile"
            drawerLayout.closeDrawers()
            navigationView.menu.getItem(1).isChecked = true
        }
    }

    private fun setUpToolbar(){
        setSupportActionBar(toolbarMainActivity)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun openHome(){
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, AllRestaurantFragment()).commit()
        supportActionBar?.title = "All Restaurants"
        navigationView.setCheckedItem(R.id.allResturant)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home){
            when(supportFragmentManager.findFragmentById(R.id.frameLayout)){
                !is MenuItemFragment ->{
                    drawerLayout.openDrawer(GravityCompat.START)
                }
                else ->{
                    foodIdList= RetrieveAllItems(this@MainActivity, 0).execute().get()
                    if (foodIdList.isEmpty()){
                        startActivity(Intent(this@MainActivity, MainActivity::class.java))
                        finish()
                    }else{
                        val dialog= AlertDialog.Builder(this@MainActivity)
                        dialog.setTitle("Confirmation")
                        dialog.setMessage("Going back will reset cart items. Do you still want to proceed?")
                        dialog.setPositiveButton("Yes"){ _, _ ->
                            RetrieveAllItems(this@MainActivity, 1).execute()
                            startActivity(Intent(this@MainActivity, MainActivity::class.java))
                            finish()
                        }
                        dialog.setNegativeButton("Cancel"){ _, _ ->

                        }
                        dialog.create()
                        dialog.show()
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        when(supportFragmentManager.findFragmentById(R.id.frameLayout)){
            !is AllRestaurantFragment ->{
                foodIdList= RetrieveAllItems(this@MainActivity, 0).execute().get()
                if (foodIdList.isEmpty()){
                    startActivity(Intent(this@MainActivity, MainActivity::class.java))
                    finish()
                }else{
                    val dialog= AlertDialog.Builder(this@MainActivity)
                    dialog.setTitle("Confirmation")
                    dialog.setMessage("Going back will reset cart items. Do you still want to proceed?")
                    dialog.setPositiveButton("Yes"){ _ , _ ->
                        RetrieveAllItems(this@MainActivity, 1).execute()
                        startActivity(Intent(this@MainActivity, MainActivity::class.java))
                        finish()
                    }
                    dialog.setNegativeButton("Cancel"){ _ , _ ->

                    }
                    dialog.create()
                    dialog.show()
                }
            }
            else -> super.onBackPressed()
        }
    }

    class RetrieveAllItems(val context: Context, private val mode: Int): AsyncTask<Void, Void, List<CartItemEntities>>(){
        private val dataTransaction = Room.databaseBuilder(context, CartItemDatabase::class.java, "cartlist-db").build()
        override fun doInBackground(vararg params: Void?): List<CartItemEntities> {
            when(mode){
                1-> {
                    dataTransaction.CartItemDao().delAllItem()
                    dataTransaction.close()
                }
            }
            return dataTransaction.CartItemDao().getAllItem()
        }
    }

    class ClearDatabase(val context: Context): AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean {
            val db = Room.databaseBuilder(context, ResturantDatabase::class.java, "restaurant-db").build()
            db.ResturantDao().delAllRes()
            db.close()
            return false
        }
    }
}
