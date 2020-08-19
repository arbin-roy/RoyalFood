package com.arbin.fastfood.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

import com.arbin.fastfood.R
import com.arbin.fastfood.adapter.FavoriteRecyclerAdapter
import com.arbin.fastfood.database.ResturantDatabase
import com.arbin.fastfood.database.ResturantEntities


class FavoriteResturantFragment : Fragment() {

    private lateinit var recyclerFavorite: RecyclerView
    private lateinit var relativeFavorite: RelativeLayout
    private lateinit var noFavRestaurants: RelativeLayout
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var recyclerAdapter: FavoriteRecyclerAdapter
    private lateinit var dbResList: List<ResturantEntities>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_favorite_resturant, container, false)
        setHasOptionsMenu(true)

        recyclerFavorite = view.findViewById(R.id.recyclerFavorite)
        relativeFavorite = view.findViewById(R.id.relativeFavorite)
        noFavRestaurants= view.findViewById(R.id.noFavRestaurants)
        layoutManager = LinearLayoutManager(activity)
        dbResList= DatabaseFavourites(activity as Context , 0).execute().get()

        if (dbResList.isEmpty()){
            noFavRestaurants.visibility = View.VISIBLE
        }else{
            noFavRestaurants.visibility = View.GONE
        }

        if (activity != null){
            relativeFavorite.visibility = View.GONE
            recyclerAdapter = FavoriteRecyclerAdapter(activity as Context, dbResList)
            recyclerFavorite.adapter= recyclerAdapter
            recyclerFavorite.layoutManager= layoutManager
        }
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fav_res_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.clear_all_from_fav -> {
                val dialog = AlertDialog.Builder(activity as Context)
                dialog.setTitle("Confirmation")
                dialog.setMessage("Do you want to clear all restaurant from Favorites?")
                dialog.setPositiveButton("Clear"){ _ , _ ->
                    if (dbResList.isEmpty()){
                        Toast.makeText(activity, "No restaurant available to clear", Toast.LENGTH_LONG).show()
                    }else {
                        dbResList = DatabaseFavourites(activity as Context, 1).execute().get()
                        noFavRestaurants.visibility = View.VISIBLE
                    }
                }
                dialog.setNegativeButton("Cancel"){ _ , _ ->

                }
                dialog.create().show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    class DatabaseFavourites(val context: Context, private val mode: Int) : AsyncTask<Void, Void, List<ResturantEntities>>() {
        override fun doInBackground(vararg params: Void?): List<ResturantEntities> {
            val db = Room.databaseBuilder(context, ResturantDatabase::class.java, "restaurant-db").build()
            when(mode){
                1 -> {
                    db.ResturantDao().delAllRes()
                    db.close()
                }
            }
            return db.ResturantDao().getAllRes()
        }
    }
}
