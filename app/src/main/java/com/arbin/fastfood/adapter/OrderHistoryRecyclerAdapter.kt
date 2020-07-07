package com.arbin.fastfood.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arbin.fastfood.R
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
        holder.childAdapter= OrderHistoryChildAdapter(context, item.menuList)
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