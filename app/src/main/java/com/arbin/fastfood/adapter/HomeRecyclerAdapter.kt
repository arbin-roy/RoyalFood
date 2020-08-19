package com.arbin.fastfood.adapter

import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.arbin.fastfood.R
//import com.arbin.fastfood.database.CartList
import com.arbin.fastfood.database.ResturantDatabase
import com.arbin.fastfood.database.ResturantEntities
import com.arbin.fastfood.fragments.MenuItemFragment
import com.arbin.fastfood.model.Restaurant
import com.squareup.picasso.Picasso
import kotlin.collections.ArrayList

class HomeRecyclerAdapter(val context: Context, val list: ArrayList<Restaurant>):RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_all_resturant, parent, false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @Suppress("NAME_SHADOWING")
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val res= list[position]
        holder.resId= res.res_id
        holder.txtResturantName.text= res.res_name
        holder.txtResturantRating.text= res.res_rating
        holder.txtPricePerPerson.text= res.cost_for_one
        Picasso.get().load(res.res_image).error(R.drawable.alert_logo).into(holder.imgResImage)

        val resDetails= list[position]
        holder.resentity= ResturantEntities(
            resDetails.res_id,
            resDetails.res_name,
            resDetails.res_rating,
            resDetails.cost_for_one,
            resDetails.res_image
        )

        if (DBAsyncTask(context, holder.resentity, 1).execute().get()){
            holder.imgbtnFavorite.setImageResource(R.drawable.ic_favorite_button_fill)
        }else{
            holder.imgbtnFavorite.setImageResource(R.drawable.ic_favorite)
        }

        holder.imgbtnFavorite.setOnClickListener {

            val resDetails= list[position]
            holder.resentity= ResturantEntities(
                resDetails.res_id,
                resDetails.res_name,
                resDetails.res_rating,
                resDetails.cost_for_one,
                resDetails.res_image
            )

            if (!DBAsyncTask(context, holder.resentity, 1).execute().get()){
                val async = DBAsyncTask(context, holder.resentity,2).execute()
                val result = async.get()

                if (result){
                    holder.imgbtnFavorite.setImageResource(R.drawable.ic_favorite_button_fill)
                }else{
                    Toast.makeText(context, "Something error occured", Toast.LENGTH_SHORT).show()
                }
            }else{
                val async = DBAsyncTask(context, holder.resentity, 3).execute()
                val result = async.get()

                if (result){
                    holder.imgbtnFavorite.setImageResource(R.drawable.ic_favorite)
                }else{
                    Toast.makeText(context, "Something error occured", Toast.LENGTH_SHORT).show()
                }

            }
        }

        holder.llcontent.setOnClickListener {
            val name= list[position]
            val activity= context as AppCompatActivity
            activity.supportFragmentManager.beginTransaction().replace(R.id.frameLayout, MenuItemFragment(holder.resId,
                name.res_name
            )).commit()
        }
    }

    class HomeViewHolder(view: View): RecyclerView.ViewHolder(view){
        val imgResImage: ImageView= view.findViewById(R.id.imgResImage)
        val txtResturantName: TextView= view.findViewById(R.id.txtResturantName)
        val txtPricePerPerson: TextView= view.findViewById(R.id.txtPricePerPerson)
        val txtResturantRating: TextView= view.findViewById(R.id.txtResturantRating)
        val llcontent: LinearLayout= view.findViewById(R.id.llContent)
        val imgbtnFavorite: ImageView= view.findViewById(R.id.imgbtnFavorite)
        lateinit var resId: String
        lateinit var resentity: ResturantEntities
    }
    class DBAsyncTask(val context: Context, private val resEntity: ResturantEntities, private val mode: Int) : AsyncTask<Void, Void, Boolean>() {

        val db = Room.databaseBuilder(context, ResturantDatabase::class.java, "restaurant-db").build()

        override fun doInBackground(vararg p0: Void?): Boolean {
            when (mode) {
                1 -> {
                    val res: ResturantEntities? = db.ResturantDao().getResById(resEntity.res_id)
                    db.close()
                    return res != null
                }
                2 -> {
                    db.ResturantDao().addRes(resEntity)
                    db.close()
                    return true
                }
                3 -> {
                    db.ResturantDao().delRes(resEntity)
                    db.close()
                    return true
                }
            }
            return false
        }
    }
}