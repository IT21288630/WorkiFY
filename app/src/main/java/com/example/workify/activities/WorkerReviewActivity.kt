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

class WorkerReviewActivity : AppCompatActivity() {


    private val ReviewCollectionRef = Firebase.firestore.collection("worker_reviews")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.worker_add_review)

        var revTitle = findViewById<EditText>(R.id.cutomerRevTitle)
        var revStar = findViewById<RatingBar>(R.id.RatingBar)
        var revDescription = findViewById<EditText>(R.id.addcustomRevDescription)

        var revRecoYes = findViewById<RadioButton>(R.id.radioRecYes)
        var revRecoNo = findViewById<RadioButton>(R.id.radioRecNo)

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


            if(revStar.rating.toInt() < 3){
                Toast.makeText(this@WorkerReviewActivity, "We Are sorry for your bad experience!", Toast.LENGTH_SHORT).show()

            }
            else{
                Toast.makeText(this@WorkerReviewActivity, "Keep going, You doing Great!", Toast.LENGTH_SHORT).show()
            }

            val message = revStar.rating.toString()

            val title = revTitle.text.toString()
            val description = revDescription.text.toString()
            val star = revStar.rating.toInt()


            var revRecommend = "yes"
            if(revRecoYes.isChecked.toString() == "true"){
                revRecommend = "Yes"
            }
            else if(revRecoNo.isChecked.toString() == "true"){
                revRecommend = "No"
            }


            var review = Review(
                revTitle.text.toString(), revRecommend, revDescription.text.toString(), revStar.rating.toInt(),
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