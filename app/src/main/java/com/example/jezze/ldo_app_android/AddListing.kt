 /***********************************************************************
 *                                                                      *
 *                            Add Listing                               *
 *                                                                      *
 *  Adds a job entry into the FireBase Database with following fields   *
 *  - Title: Title of listing                                           *
 *  - Description: details of the entry. Can be multi-line              *
 *  - Salary: Pay (per hour) of the listing. Do not omit                *
 *  - Date: date added to the database. Have it default to today's date *
 *  - Location: Where is the job located. Can be left empty             *
 *                                                                      *
 ***********************************************************************/

package com.example.jezze.ldo_app_android

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

 class AddListing : Activity() {

    /* Get the values of the EditText from the layout file */
    private var title: EditText? = null
    private var description: EditText? = null
    private var requirements: EditText? = null
    private var salary: EditText? = null

    /* Store values of EditText into strings to use in database */
    private var _titleString: String? = null
    private var _descString: String? = null
    private var _reqString: String? = null
    private var _salaryString: String? = null

    private var submitButton: Button? = null

    /* Get FireBase references */
    private var mDatabase: DatabaseReference? = null


    /* Upload to Database */
    private val uploadListener = View.OnClickListener {
        _titleString = title!!.text.toString()
        _descString = description!!.text.toString()
        _reqString = requirements!!.text.toString()
        _salaryString = salary!!.text.toString()

        /* Upload data if title and description are not empty
         * Start a new node with the _titleString as root (Change to Date!)
         *   _descString : String
         *   _salaryString : String
         *   _Requirements : ArrayList<String>*/
        if(_titleString != "" && _descString != ""){
            val dataRef = mDatabase!!.child(_titleString!!)
            dataRef.child("description").setValue(_descString)
            dataRef.child("requirements").setValue(_reqString)
            dataRef.child("salary").setValue(_salaryString)

            Toast.makeText(this@AddListing,
                    "Data uploaded successfully !",
                    Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this@AddListing,
                           "Enter the required fields!",
                            Toast.LENGTH_SHORT).show()
            /* Set hints and errors for missing required fields */
            if(_titleString == "") {
                title!!.setError("A title is required!")
                title!!.setHint("Enter a Title...")
            }
            if(_descString == "") {
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
        requirements = findViewById(R.id.requirements)
        salary = findViewById(R.id.salary)
        submitButton = findViewById(R.id.submit_button)

        mDatabase = FirebaseDatabase.getInstance().reference

//        submitButton!!.isEnabled = false

        submitButton!!.setOnClickListener(uploadListener)
    }
}
