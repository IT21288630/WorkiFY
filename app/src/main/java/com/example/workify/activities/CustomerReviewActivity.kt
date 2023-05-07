package com.example.workify.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workify.R
import com.example.workify.adapters.WorkerPendingAdapter
import com.example.workify.dataClasses.Review
import com.example.workify.dataClasses.Order
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CustomerReviewActivity : AppCompatActivity() {


    private val ReviewCollectionRef = Firebase.firestore.collection("reviews")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_add_review)

        var revTitle = findViewById<EditText>(R.id.cutomerRevTitle)
        var revStar = findViewById<RatingBar>(R.id.RatingBar)
        var revDescription = findViewById<EditText>(R.id.addcustomRevDescription)
        var revRecommend = findViewById<RadioGroup>(R.id.cusRecoRadio)
        var revEditBtn = findViewById<Button>(R.id.cusRevSubBtn)

        var curOrderID = intent.getStringExtra("orderID")
        var curCustomerEmail = intent.getStringExtra("cusEmail")


        revEditBtn.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val querySnapshot = ReviewCollectionRef
                        .whereEqualTo("cusEmail", curCustomerEmail)
                        .whereEqualTo("orderID", curOrderID)
                        .get()
                        .await()

                    for (document in querySnapshot.documents) {
                        ReviewCollectionRef.document(document.id)
                            .update("Title", revTitle.text.toString())
                        ReviewCollectionRef.document(document.id)
                            .update("Description", revDescription.text.toString())
                        ReviewCollectionRef.document(document.id)
                            .update("Star", revStar.numStars.toString())
                        ReviewCollectionRef.document(document.id)
                            .update("Recommend", revRecommend.textAlignment.toString())

                    }


                } catch (e: Exception) {
                    println(e.message)
                }
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = ReviewCollectionRef
                    .whereEqualTo("cusEmail", curCustomerEmail)
                    .whereEqualTo("orderID", curOrderID)
                    .get()
                    .await()



                for (document in querySnapshot.documents) {
                    val review = document.toObject<Review>()


                   /* println(review?.cusName)
                    println(review?.cusAddress)
                    println(review?.cusPhone)
                    println(review?.cusName)
                    println(review?.cusName)
                    println(review?.cusName)*/

                    withContext(Dispatchers.Main){
                        revTitle.setText(review?.Rtitle)
                        //revRecommend.setText(review?.Rrecomment)
                        revDescription.setText(review?.RDescription)
                        //revStar.setText(review?.star)


                    }

                }

            } catch (e: Exception) {
                println(e.message)
            }
        }
    }
}