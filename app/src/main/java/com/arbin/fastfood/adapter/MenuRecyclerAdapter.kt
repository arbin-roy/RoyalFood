package com.arbin.fastfood.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.arbin.fastfood.R
import com.arbin.fastfood.database.CartItemDatabase
import com.arbin.fastfood.database.CartItemEntities
import com.arbin.fastfood.model.MenuItem
import com.arbin.fastfood.util.Click

class MenuRecyclerAdapter(val context: Context, val list:ArrayList<MenuItem>, private val unitClicked: Click, private val resID: String):RecyclerView.Adapter<MenuRecyclerAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_menu_item, parent, false)
        return MenuViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @Suppress("NAME_SHADOWING")
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val item= list[position]
        holder.txtMenuName.text= item.itemName
        holder.txtItemPrice.text= item.itemPrice
        holder.txtMenuNumber.text= (position+1).toString()

        val id= list[position]
        holder.dataItem= CartItemEntities(
            id.foodID,
            id.itemName,
            id.itemPrice,
            resID
        )
        if (!CartitemDB(context, holder.dataItem, 1).execute().get()){
            holder.btnAdd.text= "ADD"
            val color= ContextCompat.getColor(context, R.color.colorPrimary)
            holder.btnAdd.setBackgroundColor(color)
        }else{
            holder.btnAdd.text= "REMOVE"
            val color= ContextCompat.getColor(context, R.color.goldenColor)
            holder.btnAdd.setBackgroundColor(color)
        }

        holder.btnAdd.setOnClickListener {
            val id= list[position]
            if (holder.flag){
                holder.btnAdd.text= "REMOVE"
                val color= ContextCompat.getColor(context, R.color.goldenColor)
                holder.btnAdd.setBackgroundColor(color)
                holder.flag= false
                holder.dataItem= CartItemEntities(
                    id.foodID,
                    id.itemName,
                    id.itemPrice,
                    resID
                )
                if (!CartitemDB(context, holder.dataItem, 1).execute().get()){
                    CartitemDB(context, holder.dataItem, 2).execute()
                }
            }else{
                holder.btnAdd.text= "ADD"
                val color= ContextCompat.getColor(context, R.color.colorPrimary)
                holder.btnAdd.setBackgroundColor(color)
                holder.flag= true
                holder.dataItem= CartItemEntities(
                    id.foodID,
                    id.itemName,
                    id.itemPrice,
                    resID
                )
                if (CartitemDB(context, holder.dataItem, 1).execute().get()){
                    CartitemDB(context, holder.dataItem, 3).execute()
                }
            }
            unitClicked.click()
        }
    }

    class MenuViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val txtMenuName: TextView= view.findViewById(R.id.txtMenuName)
        val txtItemPrice: TextView= view.findViewById(R.id.txtItemPrice)
        val txtMenuNumber: TextView= view.findViewById(R.id.txtMenuNumber)
        val btnAdd: Button= view.findViewById(R.id.btnAdd)
        var flag : Boolean= true
        lateinit var dataItem: CartItemEntities
    }

    class CartitemDB(val context: Context, private val cartItems: CartItemEntities, private val mode: Int): AsyncTask<Void, Void, Boolean>(){
        private val dataTransaction= Room.databaseBuilder(context, CartItemDatabase::class.java, "cartlist-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            when(mode){
                1-> {
                    val cart: CartItemEntities?= dataTransaction.CartItemDao().getItembyId(cartItems.food_item_id)
                    dataTransaction.close()
                    return cart!= null
                }
                2-> {
                    dataTransaction.CartItemDao().addItem(cartItems)
                    dataTransaction.close()
                    return true
                }
                3-> {
                    dataTransaction.CartItemDao().delItem(cartItems)
                    dataTransaction.close()
                    return true
                }
            }
            return false
        }
    }
}