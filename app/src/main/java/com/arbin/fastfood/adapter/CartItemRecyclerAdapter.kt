package com.arbin.fastfood.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arbin.fastfood.R
import com.arbin.fastfood.database.CartItemEntities

class CartItemRecyclerAdapter(val context: Context, val list: List<CartItemEntities>): RecyclerView.Adapter<CartItemRecyclerAdapter.CartItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_order_history_items, parent, false)
        return CartItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val food= list[position]
        holder.txtOrderedItemName.text= food.food_name
        holder.txtOrderedItemPrice.text= food.food_price
    }

    class CartItemViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val txtOrderedItemName: TextView = view.findViewById(R.id.txtOrderedItemName)
        val txtOrderedItemPrice: TextView = view.findViewById(R.id.txtOrderedItemPrice)
    }
}