package com.arbin.fastfood.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.arbin.fastfood.R
import com.arbin.fastfood.adapter.CartItemRecyclerAdapter
import com.arbin.fastfood.database.CartItemDatabase
import com.arbin.fastfood.database.CartItemEntities
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class CartItemFragment(val name: String) : Fragment() {

    private lateinit var recyclerCartItem: RecyclerView
    private lateinit var rlCartItem: RelativeLayout
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var recyclerAdapter: CartItemRecyclerAdapter
    private lateinit var txtcartItemResName: TextView
    private lateinit var userId: SharedPreferences
    private lateinit var btnPlaceOrder: Button
    private lateinit var foodIdList: List<CartItemEntities>
    private var sum: Int= 0

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_cart_item, container, false)

        userId= activity?.getSharedPreferences(getString(R.string.user_id), Context.MODE_PRIVATE) ?: return null
        txtcartItemResName= view.findViewById(R.id.txtcartItemResName)
        recyclerCartItem= view.findViewById(R.id.recyclerCartItem)
        btnPlaceOrder= view.findViewById(R.id.btnPlaceOrder)
        layoutManager= LinearLayoutManager(activity)
        rlCartItem= view.findViewById(R.id.rlCartItem)
        rlCartItem.visibility= View.GONE
        foodIdList= RetrieveAllItems(activity as Context).execute().get()

        for (element in foodIdList){
            sum += element.food_price.toInt()
        }

        btnPlaceOrder.text= "Place Order- Total : $sum"

        btnPlaceOrder.setOnClickListener {
            rlCartItem.visibility = View.VISIBLE
            btnPlaceOrder.visibility = View.GONE
            val queue= Volley.newRequestQueue(activity)
            val url= "http://13.235.250.119/v2/place_order/fetch_result/"
            val userid= userId.getString("ID", "none")

            val jsonArray= JSONArray()
            for (element in foodIdList){
                val data1= element.food_item_id
                val item= JSONObject()
                item.put("food_item_id", data1)
                jsonArray.put(item)
            }

            val jsonParams= JSONObject()
            jsonParams.put("user_id", userid)
            jsonParams.put("restaurant_id", foodIdList[0].restaurant_id)
            jsonParams.put("total_cost", sum.toString())
            jsonParams.put("food", jsonArray)

            val jsonRequest= object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {
                try {
                    if (it.getJSONObject("data").getBoolean("success")){
                        (activity as AppCompatActivity).supportActionBar?.title= "Successfully Placed"
                        (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(R.id.CartFrameLayout, SuccessfullScreenFragment()).commit()
                    }else{
                        Toast.makeText(activity, it.getJSONObject("data").getString("errorMessage"), Toast.LENGTH_LONG).show()
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
                    headers["Content-type"]="application/json"
                    headers["token"]="aebb3b4a51733f"
                    return headers
                }
            }
            queue.add(jsonRequest)
        }

        if (activity != null){
            txtcartItemResName.text= name
            recyclerAdapter= CartItemRecyclerAdapter(activity as Context, foodIdList)
            recyclerCartItem.adapter= recyclerAdapter
            recyclerCartItem.layoutManager= layoutManager
        }

        return view
    }

    class RetrieveAllItems(val context: Context): AsyncTask<Void, Void, List<CartItemEntities>>(){
        override fun doInBackground(vararg params: Void?): List<CartItemEntities> {
            val dataTransaction= Room.databaseBuilder(context, CartItemDatabase::class.java, "cartlist-db").build()
            return dataTransaction.CartItemDao().getAllItem()
        }
    }
}
