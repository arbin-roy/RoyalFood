package com.arbin.fastfood.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.arbin.fastfood.R
import com.arbin.fastfood.database.ResturantEntities
import com.arbin.fastfood.fragments.MenuItemFragment
import com.squareup.picasso.Picasso

class FavoriteRecyclerAdapter(val context: Context, val list: List<ResturantEntities>):RecyclerView.Adapter<FavoriteRecyclerAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_fav_restaurant, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val res = list[position]
        holder.resId= res.res_id
        holder.txtResturantName.text= res.res_name
        holder.txtResturantRating.text= res.res_rating
        holder.txtPricePerPerson.text= res.cost_for_one
        Picasso.get().load(res.res_image).into(holder.imgResImage)

        holder.llcontent.setOnClickListener {
            val activity= context as AppCompatActivity
            val name= list[position]
            activity.supportFragmentManager.beginTransaction().replace(R.id.frameLayout, MenuItemFragment(holder.resId, name.res_name)).commit()
        }
    }

    class FavoriteViewHolder(view: View): RecyclerView.ViewHolder(view){
        val imgResImage: ImageView = view.findViewById(R.id.imgresImage)
        val txtResturantName: TextView = view.findViewById(R.id.txtresturantName)
        val txtPricePerPerson: TextView = view.findViewById(R.id.txtpriceperPerson)
        val txtResturantRating: TextView = view.findViewById(R.id.txtresturantRating)
        val llcontent: LinearLayout = view.findViewById(R.id.llcontent)
        lateinit var resId: String
    }
}