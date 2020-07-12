package com.arbin.fastfood.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arbin.fastfood.R
import com.arbin.fastfood.model.MenuItem

class OrderHistoryChildAdapter(val context: Context, private val foodList: ArrayList<MenuItem>): RecyclerView.Adapter<OrderHistoryChildAdapter.ChildViewHolder>() {

    class ChildViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val txtOrderedItemName: TextView = view.findViewById(R.id.txtOrderedItemName)
        val txtOrderedItemPrice: TextView = view.findViewById(R.id.txtOrderedItemPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_order_history_items, parent, false)
        return ChildViewHolder(view)
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        val item= foodList[position]
        holder.txtOrderedItemName.text = item.itemName
        holder.txtOrderedItemPrice.text = item.itemPrice
    }
}