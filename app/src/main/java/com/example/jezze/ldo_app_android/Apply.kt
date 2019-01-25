package com.example.jezze.ldo_app_android

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
//import java.nashorn.internal.runtime.ScriptingFunctions.readLine
import android.provider.MediaStore
import android.util.Log
import java.io.*
import java.net.URI


class Apply : AppCompatActivity() {

    /* Attachment Info */
    private val WRITE_REQUEST_CODE: Int = 42
    private var fileName : String? = null
    private var filePath : String? = null
    private var myFile : File? = null
    private var fileContent : String? = null

    private var jobTitle : String? = null

    /* Applicant details */
    private var firstName : String? = null
    private var lastName : String? = null
    private var email : String? = null
    private var phone : String? = null
    private var extraInfo: String? = null

    /* Request for permissions */
    private var MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL : Int = 0


    private fun openFile(){
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL)

        } else {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/pdf"
            }

            startActivityForResult(intent, WRITE_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // WRITE_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == WRITE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            var uri : Uri? = null
            if(resultData != null){
//                val docPathTV = findViewById<TextView>(R.id.doc_path)

                uri = resultData.data
                getFileName(uri)
                myFile = File(uri.path)
                saveFile(uri)
//                docPathTV.text = filePath

            }
        }
    }

    private fun getFileName (uri : Uri) {
        var uriName : String? = null
        val cursor: Cursor? = contentResolver.query(
                uri,
                null,
                null,
                null,
                null,
                null
        )

        cursor?.use {
            if(it.moveToFirst()){
                uriName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                val docNameTV = findViewById<TextView>(R.id.doc_name)
                docNameTV.text = uriName
                fileName = uriName
            }
        }
    }

    private fun saveFile (uri : Uri){
        val newFile = File(this.filesDir, fileName)
        filePath = newFile.absolutePath

        Log.d("jezze", "New file path: " + filePath)

        val fos = FileOutputStream(newFile)
        val bos = BufferedOutputStream(fos)
        val inStream = contentResolver.openInputStream(uri)

        try {
            val buffer = ByteArray(8192)
            var len = inStream.read(buffer)
            Log.d ("jezze", "This is where I am")
            while(len > 0){
                Log.d("jezze", "Len: " + len )
                bos.write(buffer, 0, len)
                len = inStream.read(buffer)
            }

            bos.flush()
        }
        finally {
            Log.d("jezze", "And i'm out")
            fos.fd.sync()
            bos.close()
            inStream.close()
        }
    }

    private fun readFileContent(uri : Uri) : String{
        val inputStream = contentResolver.openInputStream(uri)
        val reader = BufferedReader(InputStreamReader(
                inputStream) as Reader?)
        val stringBuilder = StringBuilder()
        var currentline: String? = reader.readLine()
        while (currentline != null) {
            stringBuilder.append(currentline + "\n")
            currentline = reader.readLine()
        }
        inputStream!!.close()
        return stringBuilder.toString()
    }


    /* Sending Email from noreply_ldoapp@ldostaffingsolutions.com
     * Launched after hitting Apply Button
     * Change Settings in SendMail.kt and Config.kt */
    private fun sendApp() {

        //Get applicant details
        firstName = findViewById<EditText>(R.id.input_first_name)?.text.toString()
        lastName = findViewById<EditText>(R.id.input_last_name)?.text.toString()
        email = findViewById<EditText>(R.id.email_input)?.text.toString()
        phone = findViewById<EditText>(R.id.phone_input)?.text.toString()


        val subject : String = "LDO App Application: " + jobTitle
        val message : String =
                "Applying for Position: " + jobTitle +
                "\n\nApplicant name: " + lastName + ", " + firstName +
                "\n\nEmail: " + email + "\n\nPhone: " + phone

        val context : Context = this

        val sm = SendMail(this, subject, message, fileName, myFile, filePath)

        sm.execute()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply)

        val extras: Bundle = intent.extras
        jobTitle = extras.getString("job_title")

        val titleTV = findViewById<TextView>(R.id.apply_title)
        titleTV.text = "Applying for " + jobTitle

        //Do the upload resume button stuff
        val uploadButton = findViewById<Button>(R.id.upload_button)

        uploadButton.setOnClickListener{
            openFile()
        }

        //Application Button
        val applyButton = findViewById<Button>(R.id.send_button)

        applyButton.setOnClickListener{
            sendApp()
        }
    }
}
