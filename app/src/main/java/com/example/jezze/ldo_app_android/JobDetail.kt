package com.example.jezze.ldo_app_android

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

class JobDetail : AppCompatActivity() {

    var detail_title: String? = null
    var detail_description: String? = null
    var detail_salary: String? = null
    var detail_reqs: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_detail)

        setSupportActionBar(findViewById(R.id.my_toolbar))

        val extras: Bundle = intent.extras
        detail_title = extras.getString("detail_title")
        detail_description = extras.getString("detail_description")
        detail_salary = extras.getString("detail_salary")
        detail_reqs = extras.getString("detail_requirements")

        val titleTV = findViewById<TextView>(R.id.detail_title)
        val descriptionTV = findViewById<TextView>(R.id.detail_description)
        val salaryTV = findViewById<TextView>(R.id.detail_salary)
        val reqTV = findViewById<TextView>(R.id.detail_requirement)

        titleTV.text = detail_title
        descriptionTV.text = detail_description
        salaryTV.text = detail_salary
        reqTV.text = detail_reqs


    }
}
