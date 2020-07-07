package com.arbin.fastfood.fragments

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
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

        recyclerFavorite = view.findViewById(R.id.recyclerFavorite)
        relativeFavorite = view.findViewById(R.id.relativeFavorite)
        noFavRestaurants= view.findViewById(R.id.noFavRestaurants)
        layoutManager = LinearLayoutManager(activity)
        dbResList= RetrieveFavourites(activity as Context).execute().get()

        if (dbResList.isEmpty()){
            noFavRestaurants.visibility= View.VISIBLE
        }else{
            noFavRestaurants.visibility= View.GONE
        }

        if (activity != null){
            relativeFavorite.visibility= View.GONE
            recyclerAdapter = FavoriteRecyclerAdapter(activity as Context, dbResList)
            recyclerFavorite.adapter= recyclerAdapter
            recyclerFavorite.layoutManager= layoutManager
        }
        return view
    }

    class RetrieveFavourites(val context: Context) : AsyncTask<Void, Void, List<ResturantEntities>>() {
        override fun doInBackground(vararg params: Void?): List<ResturantEntities> {
            val db = Room.databaseBuilder(context, ResturantDatabase::class.java, "restaurant-db").build()
            return db.ResturantDao().getAllRes()
        }
    }
}
