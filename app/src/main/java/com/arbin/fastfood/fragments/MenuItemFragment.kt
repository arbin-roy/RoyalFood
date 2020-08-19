package com.arbin.fastfood.fragments

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.arbin.fastfood.R
import com.arbin.fastfood.activities.CartActivity
import com.arbin.fastfood.adapter.MenuRecyclerAdapter
import com.arbin.fastfood.database.CartItemDatabase
import com.arbin.fastfood.database.CartItemEntities
import com.arbin.fastfood.model.MenuItem
import com.arbin.fastfood.util.Click
import org.json.JSONException

class MenuItemFragment(val data: String, val name: String) : Fragment(), Click{

    lateinit var menuitemRecycler: RecyclerView
    lateinit var recyclerAdapter: MenuRecyclerAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var toolbarMenuItem: Toolbar
    lateinit var rlMenuItem: RelativeLayout
    lateinit var menuItemSomethingWrong: RelativeLayout
    private lateinit var btnGoToCart: Button
    lateinit var id: String
    var itemList = ArrayList<MenuItem>()
    private lateinit var foodIdList: List<CartItemEntities>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_menu_item, container, false)

        menuitemRecycler= view.findViewById(R.id.menuitemRecycler)
        layoutManager= LinearLayoutManager(activity)
        rlMenuItem= view.findViewById(R.id.rlMenuItem)
        menuItemSomethingWrong= view.findViewById(R.id.menuItemSomethingWrong)
        menuItemSomethingWrong.visibility = View.GONE
        btnGoToCart= view.findViewById(R.id.btnGotoCart)
        btnGoToCart.visibility= View.GONE
        toolbarMenuItem= view.findViewById(R.id.toolbarMenuItem)

        (activity as AppCompatActivity).supportActionBar?.hide()
        setUpToolBar()

        val queue= Volley.newRequestQueue(activity)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/$data"

        val jsonRequest= object : JsonObjectRequest(Method.GET, url, null, Response.Listener {
            try {
                if (it.getJSONObject("data").getBoolean("success")){
                    rlMenuItem.visibility= View.GONE
                    val data= it.getJSONObject("data").getJSONArray("data")
                    for (i in 0 until data.length()){
                        val dataArray= data.getJSONObject(i)
                        val restaurantId = dataArray.getString("restaurant_id")
                        val dataObject= MenuItem(
                            dataArray.getString("id"),
                            dataArray.getString("name"),
                            dataArray.getString("cost_for_one")
                        )
                        itemList.add(dataObject)
                        recyclerAdapter= MenuRecyclerAdapter(activity as Context, itemList, this, restaurantId)
                        menuitemRecycler.adapter= recyclerAdapter
                        menuitemRecycler.layoutManager= layoutManager
                    }
                }
            }catch (e: JSONException){
                Toast.makeText(activity as Context, "JSONException", Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener {
            rlMenuItem.visibility= View.GONE
            menuItemSomethingWrong.visibility = View.VISIBLE
        }){
            override fun getHeaders(): MutableMap<String, String>{
                val headers= HashMap<String, String>()
                headers["Content-type"]= "application/json"
                headers["token"]="aebb3b4a51733f"
                return headers
            }
        }
        queue.add(jsonRequest)
        return view
    }

    private fun setUpToolBar(){
        val activity= activity as AppCompatActivity
        activity.setSupportActionBar(toolbarMenuItem)
        activity.supportActionBar?.title= name
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun click() {
        foodIdList= RetrieveAllItems(activity as Context).execute().get()
        if (foodIdList.isNotEmpty()){
            btnGoToCart.visibility = View.VISIBLE
        }else{
            btnGoToCart.visibility = View.GONE
        }
        btnGoToCart.setOnClickListener {
            val intent = Intent(activity, CartActivity::class.java)
            intent.putExtra("resName", name)
            startActivity(intent)
        }
    }

    class RetrieveAllItems(val context: Context): AsyncTask<Void, Void, List<CartItemEntities>>(){
        override fun doInBackground(vararg params: Void?): List<CartItemEntities> {
            val dataTransaction= Room.databaseBuilder(context, CartItemDatabase::class.java, "cartlist-db").build()
            return dataTransaction.CartItemDao().getAllItem()
        }
    }
}
