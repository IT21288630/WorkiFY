package com.example.workify.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
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
        val ratingScale = findViewById<TextView>(R.id.ratingBarText)

        var curCustomerEmail = intent.getStringExtra("cusEmail")
        var workerEmail = intent.getStringExtra("workerEmail")

        revStar.setOnRatingBarChangeListener { ratingBar, fl, b ->
            ratingScale.text = fl.toString()
            when(ratingBar.rating.toInt()){
                1 -> ratingScale.text = "Very Bad"
                2 -> ratingScale.text = "Bad"
                3 -> ratingScale.text = "Good"
                4 -> ratingScale.text = "Very Good"
                5 -> ratingScale.text = "Love It"
                else -> ratingScale.text = " "

            }

        }


        revAddBtn.setOnClickListener {

            val message = revStar.rating.toString()
            Toast.makeText(this@CustomerReviewActivity, "Rating is: "+message, Toast.LENGTH_LONG).show()
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