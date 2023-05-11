package com.example.workify.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.R
import com.example.workify.adapters.WorkerReviewsForCustomerProfileAdapter
import com.example.workify.dataClasses.Review
import com.google.firebase.database.*

class WorkerReviewShowActivity:AppCompatActivity() {
    private lateinit var dbref: DatabaseReference
    private lateinit var userRecylerview: RecyclerView
    private lateinit var userArrayList: ArrayList<Review>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_worker_profile_reviews)

        userRecylerview = findViewById(R.id.rvCustomerReviewsForWorkerProfile)
        userRecylerview.layoutManager = LinearLayoutManager(this)
        userRecylerview.setHasFixedSize(true)

        userArrayList = arrayListOf<Review>()
        getUserData()

    }

    private fun getUserData() {
        dbref = FirebaseDatabase.getInstance().getReference("worker_reviews")
        dbref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(userSnapshot in snapshot.children){

                        val user = userSnapshot.getValue(Review::class.java)
                        userArrayList.add(user!!)
                    }

                    userRecylerview.adapter = WorkerReviewsForCustomerProfileAdapter(userArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}