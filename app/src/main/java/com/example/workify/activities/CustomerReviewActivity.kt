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


    private val ReviewCollectionRef = Firebase.firestore.collection("customer_reviews")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_add_review)

        var revTitle = findViewById<EditText>(R.id.cutomerRevTitle)
        var revStar = findViewById<RatingBar>(R.id.RatingBar)
        var revDescription = findViewById<EditText>(R.id.addcustomRevDescription)
        var revRecommend = findViewById<RadioGroup>(R.id.cusRecoRadio)
        var revAddBtn = findViewById<Button>(R.id.cusRevSubBtn)

        var curCustomerEmail = intent.getStringExtra("cusEmail")
        var workerEmail = intent.getStringExtra("workerEmail")


        revAddBtn.setOnClickListener {

            val title = revTitle.text.toString()
            val description = revDescription.text.toString()


            var review = Review(
                revTitle.text.toString(), revRecommend.toString(), revDescription.text.toString(), 4,
                "workerEmail.toString()", "curCustomerEmail.toString()"
            )

            CoroutineScope(Dispatchers.IO).launch {

                println("this is the coroutinescope dispatcher")
                try {
                    val querySnapshot = ReviewCollectionRef
                        .add(review)
                        .await()


                    /* for (document in querySnapshot.documents) {
                         ReviewCollectionRef.document(document.id)
                             .update("Title", revTitle.text.toString())
                         ReviewCollectionRef.document(document.id)
                             .update("Description", revDescription.text.toString())
                         ReviewCollectionRef.document(document.id)
                             .update("Star", revStar.numStars.toString())
                         ReviewCollectionRef.document(document.id)
                             .update("Recommend", revRecommend.textAlignment.toString())

                     }
 */

                } catch (e: Exception) {
                    println(e.message)
                }
            }
        }


    }

}