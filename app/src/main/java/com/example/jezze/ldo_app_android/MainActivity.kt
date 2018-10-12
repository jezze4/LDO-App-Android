package com.example.jezze.ldo_app_android

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //add addjob button
        val button_addjob = findViewById<Button>(R.id.add_button)


        button_addjob.setOnClickListener() {
            val intent = Intent(this, AddListing::class.java)
            startActivity(intent)
        }
    }
}
