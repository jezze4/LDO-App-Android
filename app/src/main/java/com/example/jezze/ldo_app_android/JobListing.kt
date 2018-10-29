package com.example.jezze.ldo_app_android

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

import java.util.ArrayList
import java.util.Collections

class JobListing : Activity() {

    private val databaseReference = FirebaseDatabase.getInstance().getReference("data")
    private var titles : ArrayList<String>? = null
    private var descriptions : ArrayList<String>? = null
    private var salaries : ArrayList<String>? = null
    private var dates : ArrayList<String>? = null
    private var locations : ArrayList<String>? = null
    private var adapter : FeedAdapter? = null
    private var item_listing : LinearLayout = findViewById(R.id.item_box)

    private val eventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            refresh()
            for (titleData in dataSnapshot.children){
                titles!!.add(titleData.key as String)
                for(jobData in titleData.children){
                    val value = jobData.getValue(String::class.java) as String
                    if (jobData.key == "description")
                        descriptions!!.add(value)
                    if (jobData.key == "salary")
                        salaries!!.add(value)
                }
            }
        }

        override fun onCancelled(p0: DatabaseError) {
            TODO("not implemented")
            //To change body of created functions use File | Settings | File Templates.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_listing)

        titles = ArrayList()
        dates = ArrayList()
        locations = ArrayList()

        setUpRecyclerView()
    }

    private fun refresh(){
        titles!!.clear()
        dates!!.clear()
        locations!!.clear()
    }

    private fun setUpRecyclerView() {
        val recyclerView = findViewById<View>(R.id.item_list) as RecyclerView
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayout.VERTICAL
        recyclerView.layoutManager = layoutManager
        adapter = FeedAdapter(this,
                                titles as List<String>,
                                locations as List<String>,
                                dates as List<String>)

    }

    override fun onPause() {
        super.onPause()
        databaseReference.removeEventListener(eventListener)
    }

    override fun onResume() {
        super.onResume()
        refresh()
        databaseReference.addValueEventListener(eventListener)
    }

    /* ************************************************************
    ********* Use if need a button on top right of screen *********

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuinflater.inflate(R.menu.(MENU.XML), menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.MENU_ITEM)
            startActivity(Intent(this, LAUNCH_ACTIVITY::class.java))

        return super.onOptionsItemSelected(item)
    }
    *****************************************************************/

    inner class FeedHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        internal var feedTitle: TextView =
                itemView.findViewById<View>(R.id.item_title) as TextView
        internal var feedLocation: TextView =
                itemView.findViewById<View>(R.id.item_location) as TextView
        internal var feedDate: TextView =
                itemView.findViewById<View>(R.id.item_date) as TextView

    }

    inner class FeedAdapter(private val context: Context,
                            private val titles: List<String>,
                            private val locations: List<String>,
                            private val dates: List<String>)
                            :RecyclerView.Adapter<FeedHolder>(){
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FeedHolder {
            val rootView = LayoutInflater
                    .from(viewGroup.context)
                    .inflate(R.layout.itemlisting, null)
            return FeedHolder(rootView)
        }

        override fun onBindViewHolder(holder: FeedHolder, pos: Int) {
            holder.feedTitle.text = titles[pos]
            holder.feedLocation.text = locations[pos]
            holder.feedDate.text = dates[pos]

            findViewById<LinearLayout>(R.id.item_box).setOnClickListener(){
                Toast.makeText(this@JobListing,
                        "You clicked on the Toaster",
                        Toast.LENGTH_SHORT).show()
            }
        }
        override fun getItemCount(): Int {
            return titles.size
        }
    }
}
