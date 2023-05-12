package com.example.workify.activities

import android.os.Bundle
import android.widget.RadioGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.example.workify.R
import com.example.workify.dataClasses.Order
import com.example.workify.dataClasses.Review
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class ViewRevCusActivity : FragmentActivity() {

    private val reviewCollectionRef = Firebase.firestore.collection("customer_reviews")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_rev_cus)


        var RatingBar = findViewById<RatingBar>(R.id.RatingBar)
        var title = findViewById<TextView>(R.id.cutomerRevTitle)
        var etAddress = findViewById<RadioGroup>(R.id.radioGroup)
        var description = findViewById<TextView>(R.id.addcustomRevDescription)

        var curCusEmail = intent.getStringExtra("orderID")
        var curWorkerEmail = intent.getStringExtra("workEmail")

        println(curCusEmail)
        println(curWorkerEmail)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = reviewCollectionRef
                    .whereEqualTo("worker_email", curWorkerEmail)
                    .whereEqualTo("customer_email", curCusEmail)
                    .get()
                    .await()



                for (document in querySnapshot.documents) {
                    val review = document.toObject<Review>()

                    println(review?.customer_email)
                    println(review?.worker_email)
                    println(review?.title)
                    println(review?.recomment)


                    if(review != null){
                        withContext(Dispatchers.Main){
                           // curCusEmail.text = review.customer_email
                          //  curWorkerEmail.text = review.worker_email
                            title.text = review.title
                            description.text = review.description
                            RatingBar.rating = review.stars.toFloat()

                        }
                    }


                }

            } catch (e: Exception) {
                println(e.message)
            }
        }

    }


}