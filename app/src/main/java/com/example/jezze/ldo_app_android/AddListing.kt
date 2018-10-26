package com.example.jezze.ldo_app_android

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_listing.*
import java.sql.Types.NULL

class AddListing : Activity() {

    /* Get the values of the EditText from the layout file */
    private var title: EditText? = null
    private var description: EditText? = null
    private var salary: EditText? = null

    /* Store values of EditText into strings to use in database */
    private var title_string: String? = null
    private var desc_string: String? = null
    private var salary_string: String? = null

    private var submitButton: Button? = null

    /* Get FireBase references */
    private var mDatabase: DatabaseReference? = null


    private val uploadListener = View.OnClickListener {
        title_string = title!!.text.toString()
        desc_string = description!!.text.toString()
        salary_string = salary!!.text.toString()

        if(title_string != "" && desc_string != ""){
            val dataRef = mDatabase!!.child(title_string!!)
            dataRef.child("description").setValue(desc_string)
            dataRef.child("salary").setValue(salary_string)

            Toast.makeText(this@AddListing,
                    "Data uploaded successfully !",
                    Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this@AddListing,
                           "Enter the required fields!",
                            Toast.LENGTH_SHORT).show()
            /* Set hints and errors for missing required fields */
            if(title_string == "") {
                title!!.setError("A title is required!")
                title!!.setHint("Enter a Title...")
            }
            if(desc_string == "") {
                description!!.setError("A description is required!")
                description!!.setHint("Enter a description...")
            }

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_listing)

        title = findViewById(R.id.title)
        description = findViewById(R.id.description)
        salary = findViewById(R.id.salary)
        submitButton = findViewById(R.id.submit_button)

        mDatabase = FirebaseDatabase.getInstance().reference

//        submitButton!!.isEnabled = false

        submitButton!!.setOnClickListener(uploadListener)
    }
}
