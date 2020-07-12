package com.arbin.fastfood.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.arbin.fastfood.R
import com.arbin.fastfood.adapter.OrderHistoryRecyclerAdapter
import com.arbin.fastfood.model.MenuItem
import com.arbin.fastfood.model.OrderedRestaurantDetails
import org.json.JSONException


class OrderHistoryFragment : Fragment() {

    private lateinit var userID: SharedPreferences
    lateinit var recyclerOrderHistory: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: OrderHistoryRecyclerAdapter
    lateinit var rlOrderHistory: RelativeLayout
    lateinit var noOrderHistory: RelativeLayout
    //var itemList = ArrayList<MenuItem>()
    var nameList = ArrayList<OrderedRestaurantDetails>()
    private var iD: String? ="Not Found"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_order_history, container, false)

        recyclerOrderHistory= view.findViewById(R.id.recyclerOrderHistory)
        layoutManager= LinearLayoutManager(activity)
        rlOrderHistory= view.findViewById(R.id.rlOrderHistory)
        noOrderHistory= view.findViewById(R.id.noOrderHistory)
        userID= activity?.getSharedPreferences(getString(R.string.user_id), Context.MODE_PRIVATE) ?: return null
        iD = userID.getString("ID", "Not Found")

        val queue= Volley.newRequestQueue(activity)
        val url = "http://13.235.250.119/v2/orders/fetch_result/$iD"

        val jsonRequest= object : JsonObjectRequest(Method.GET, url, null, Response.Listener {
            try {
                if (it.getJSONObject("data").getBoolean("success")){
                    rlOrderHistory.visibility = View.GONE
                    val data= it.getJSONObject("data").getJSONArray("data")
                    for (i in 0 until data.length()){
                        val dataObject= data.getJSONObject(i)
                        val foodArray= dataObject.getJSONArray("food_items")
                        /*for (j in 0 until foodArray.length()){
                            val foodDetails= foodArray.getJSONObject(j)
                            val details = MenuItem(
                                foodDetails.getString("food_item_id"),
                                foodDetails.getString("name"),
                                foodDetails.getString("cost")
                            )
                            itemList.add(details)
                        }*/
                        val restaurantName = OrderedRestaurantDetails(
                            dataObject.getString("restaurant_name"),
                            dataObject.getString("order_placed_at"),
                            foodArray
                        )

                        nameList.add(restaurantName)
                        recyclerAdapter= OrderHistoryRecyclerAdapter(activity as Context, nameList)
                        recyclerOrderHistory.adapter= recyclerAdapter
                        recyclerOrderHistory.isNestedScrollingEnabled = false
                        recyclerOrderHistory.layoutManager= layoutManager

                        if (nameList.isEmpty()){
                            noOrderHistory.visibility= View.VISIBLE
                        }else{
                            noOrderHistory.visibility= View.GONE
                        }
                    }
                }
            }catch (e: JSONException){
                Toast.makeText(activity, "JSONException", Toast.LENGTH_LONG).show()
            }
        }, Response.ErrorListener {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Connection Established failed")
            dialog.setPositiveButton("FAQ"){ _, _ ->
                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.frameLayout, FAQFragment())?.commit()
            }

            dialog.setNegativeButton("Exit") { _, _ ->
                activity?.finishAffinity()
            }
            dialog.create()
            dialog.show()
        }){
            override fun getHeaders(): MutableMap<String, String> {
                val headers= HashMap<String, String>()
                headers["Content-type"]= "application/json"
                headers["token"]="aebb3b4a51733f"
                return headers
            }
        }
        queue.add(jsonRequest)
        return view
    }
}
