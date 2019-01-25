/***********************************************************************
*                                                                      *
*                             Main Menu                                *
*                                                                      *
* This Kotlin file for the Admin-level app. contains 2 Buttons:        *
*  - Add Job: Links to AddListing.kt to add jobs to Firebase Database  *
*  - Find Jobs: Links to JobListing.kt - the User-level app            *
*                                                                      *
***********************************************************************/

package com.example.jezze.ldo_app_android

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : Activity() {
    private val databaseReference = FirebaseDatabase.getInstance().reference

    private val eventListener  = object : ValueEventListener {

        override fun onDataChange(dataSnapshot: DataSnapshot) {

        }

        override fun onCancelled(p0: DatabaseError) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //add addjob button
        val button_addjob = findViewById<Button>(R.id.add_button)
        val button_search = findViewById<Button>(R.id.search_button)

        button_addjob.setOnClickListener {
            val intent = Intent(this, AddListing::class.java)
            startActivity(intent)
        }
        button_search.setOnClickListener {
            val intent = Intent(this, JobListing::class.java)
            startActivity(intent)
        }
    }
}
