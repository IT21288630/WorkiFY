package com.example.workify.activities

// for customer from worker
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.R
import com.example.workify.adapters.WorkerReviewsForCustomerProfileAdapter
import com.example.workify.dataClasses.Review
import com.google.firebase.database.*

class CusReviewListActivity:AppCompatActivity() {

    private lateinit var dbref : DatabaseReference
    private lateinit var userRecyclerview : RecyclerView
    private lateinit var userArrayList : ArrayList<Review>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_customer_review)

        userRecyclerview = findViewById(R.id.rvCustomer_review_list)
        userRecyclerview.layoutManager = LinearLayoutManager(this)
        userRecyclerview.setHasFixedSize(true)

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
                    userRecyclerview.adapter = WorkerReviewsForCustomerProfileAdapter(userArrayList)
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }


}