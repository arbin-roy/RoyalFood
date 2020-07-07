package com.arbin.fastfood.fragments

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.arbin.fastfood.R
import com.arbin.fastfood.adapter.HomeRecyclerAdapter
import com.arbin.fastfood.model.Restaurant
import com.arbin.fastfood.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class AllRestaurantFragment : Fragment() {

    lateinit var recyclerAdapter: HomeRecyclerAdapter
    lateinit var recyclerAllRes: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var progressLayout: RelativeLayout
    var resInfoList = ArrayList<Restaurant>()
    private var checkedItem: Int = 0

    private val costComparator = Comparator<Restaurant>{ res1, res2 ->
        if (res1.cost_for_one.compareTo(res2.cost_for_one) == 0){
            res1.res_name.compareTo(res2.res_name)
        }else{
            res1.cost_for_one.compareTo(res2.cost_for_one)
        }
    }
    private val ratingComparator = Comparator<Restaurant>{ res1, res2 ->
        if (res1.res_rating.compareTo(res2.res_rating) == 0){
            res1.res_name.compareTo(res2.res_name)
        }else{
            res1.res_rating.compareTo(res2.res_rating)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        val view= inflater.inflate(R.layout.fragment_all_restaurant, container, false)

        recyclerAllRes= view.findViewById(R.id.recyclerAllRes)
        layoutManager= LinearLayoutManager(activity)
        progressLayout= view.findViewById(R.id.progressLayout)

        val queue= Volley.newRequestQueue(activity)
        val url= "http://13.235.250.119/v2/restaurants/fetch_result/"

        if (ConnectionManager().checkConnection(activity as Context)){
            val jsonObjectRequest= object : JsonObjectRequest(Method.GET, url, null, Response.Listener {
                try {
                    progressLayout.visibility= View.GONE
                    if (it.getJSONObject("data").getBoolean("success")){
                        val data= it.getJSONObject("data").getJSONArray("data")
                        for (i in 0 until data.length()){
                            val resJsonObject= data.getJSONObject(i)
                            val restaurantObject= Restaurant(
                                resJsonObject.getString("id"),
                                resJsonObject.getString("name"),
                                resJsonObject.getString("rating"),
                                resJsonObject.getString("cost_for_one"),
                                resJsonObject.getString("image_url")
                            )
                            resInfoList.add(restaurantObject)
                            recyclerAdapter = HomeRecyclerAdapter(activity as Context, resInfoList)

                            recyclerAllRes.adapter = recyclerAdapter
                            recyclerAllRes.layoutManager = layoutManager
                        }
                    }
                }catch (e: JSONException){
                    Toast.makeText(activity as Context, "JSONException $it", Toast.LENGTH_LONG).show()
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
            queue.add(jsonObjectRequest)
        }else{
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection not Found")
            dialog.setPositiveButton("Open Settings"){ _, _ ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }

            dialog.setNegativeButton("Exit") { _, _ ->
                activity?.finishAffinity()
            }
            dialog.create()
            dialog.show()
        }
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sort_restaurant, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.sort_restaurants -> {
                val dialog = AlertDialog.Builder(activity as Context)
                val optionNames= arrayOf("Cost(Low to High)", "Cost(High to Low)", "Rating")
                dialog.setTitle("Sort by?")
                dialog.setSingleChoiceItems(optionNames, checkedItem, DialogInterface.OnClickListener(
                    fun(_: DialogInterface, option: Int){
                        when(option){
                            0 -> {
                                Collections.sort(resInfoList, costComparator)
                                checkedItem = 0
                            }
                            1 -> {
                                Collections.sort(resInfoList, costComparator)
                                resInfoList.reverse()
                                checkedItem = 1
                            }
                            2 -> {
                                Collections.sort(resInfoList, ratingComparator)
                                resInfoList.reverse()
                                checkedItem = 2
                            }
                        }
                    }
                ))
                dialog.setPositiveButton("Sort"){ _ , _ ->
                    if (checkedItem == 0){
                        Collections.sort(resInfoList, costComparator)
                        recyclerAdapter.notifyDataSetChanged()
                    }else{
                        recyclerAdapter.notifyDataSetChanged()
                    }
                }
                dialog.setNegativeButton("Cancel"){ _ , _ ->

                }
                dialog.create()
                dialog.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
