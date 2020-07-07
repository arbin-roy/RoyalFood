package com.arbin.fastfood.fragments

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.room.Room
import com.arbin.fastfood.R
import com.arbin.fastfood.activities.MainActivity
import com.arbin.fastfood.database.CartItemDatabase

class SuccessfullScreenFragment : Fragment() {

    private lateinit var btnReturnHome : Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_successfull_screen, container, false)

        btnReturnHome= view.findViewById(R.id.btnReturnHome)
        btnReturnHome.setOnClickListener {
            ClearDatabase(activity as Context).execute()
            val intent= Intent(activity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            activity?.finish()
        }

        return view
    }

    class ClearDatabase(val context: Context): AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean {
            val db = Room.databaseBuilder(context, CartItemDatabase::class.java, "cartlist-db").build()
            db.CartItemDao().delAllItem()
            db.close()
            return false
        }
    }
}
