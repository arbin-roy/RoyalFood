package com.arbin.fastfood.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.arbin.fastfood.R

class AboutAppFragment : Fragment() {

    private lateinit var sendEmail: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_about_app, container, false)

        sendEmail = view.findViewById(R.id.sendEmail1)
        sendEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "arbin03.01@gmail.com", null))
            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback about RoyalFood")
            startActivity(intent)
            //startActivity(Intent.createChooser(intent, "Choose an Email client"))
        }

        return view
    }

}