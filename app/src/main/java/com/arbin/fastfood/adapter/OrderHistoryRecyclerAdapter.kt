package com.arbin.fastfood.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arbin.fastfood.R
import com.arbin.fastfood.model.MenuItem
import com.arbin.fastfood.model.OrderedRestaurantDetails

class OrderHistoryRecyclerAdapter(val context: Context, private val restaurantName: ArrayList<OrderedRestaurantDetails>): RecyclerView.Adapter<OrderHistoryRecyclerAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_order_history, parent, false)
        return OrderViewHolder(view)
    }

    override fun getItemCount(): Int {
        return restaurantName.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val item = restaurantName[position]
        holder.txtOrderedRestaurantName.text= item.restaurantName
        val i = item.orderedDate.indexOf(' ')
        holder.txtOrderedDate.text= item.orderedDate.substring(0, i)
        holder.layoutManager= LinearLayoutManager(context)

        val foodItem = ArrayList<MenuItem>()
        val orderHistoryObject = restaurantName[position]
        val foodArray = orderHistoryObject.foodList
        for (data in 0 until orderHistoryObject.foodList.length()){
            val foodObject = foodArray.getJSONObject(data)
            val details = MenuItem(
                foodObject.getString("food_item_id"),
                foodObject.getString("name"),
                foodObject.getString("cost")
            )
            foodItem.add(details)
        }

        holder.childAdapter= OrderHistoryChildAdapter(context, foodItem)
        holder.childRecycler.adapter= holder.childAdapter
        holder.childRecycler.layoutManager= holder.layoutManager
    }

    class OrderViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val txtOrderedRestaurantName: TextView= view.findViewById(R.id.txtOrderedRestaurantName)
        val txtOrderedDate: TextView= view.findViewById(R.id.txtOrderedDate)
        val childRecycler: RecyclerView = view.findViewById(R.id.childRecycler)
        lateinit var layoutManager: RecyclerView.LayoutManager
        lateinit var childAdapter: OrderHistoryChildAdapter
    }
}