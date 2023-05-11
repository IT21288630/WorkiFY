package com.example.workify.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.workify.R
import com.example.workify.dataClasses.Order
import com.example.workify.dataClasses.Review
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CustomerEditReviewActivity : AppCompatActivity() {

    private var CustomerEmail = "qwer"
    private  var workerEmail ="bnb"

    private val TAG = "CustomerEditReviewActivity"

    private lateinit var revTitle: EditText
    private lateinit var revDescription : EditText
    private lateinit var revStar : RatingBar

    //private lateinit var progressBar : ProgressBar

    private var db = Firebase.firestore

    private val customerCollectionRef = Firebase.firestore.collection("worker_reviews")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cutomer_edit_review)

        revTitle = findViewById(R.id.cutomerRevTitle)
        revStar = findViewById(R.id.RatingBar)
        revDescription = findViewById(R.id.addcustomRevDescription)

        var revRecoYes = findViewById<RadioButton>(R.id.radioRecYes)
        var revRecoNo = findViewById<RadioButton>(R.id.radioRecNo)

        var revUpdateBtn = findViewById<Button>(R.id.editReviewUpdateBtn)
        var revCancelBtn = findViewById<Button>(R.id.editReviewCancelBtn)

        val ratingScale = findViewById<TextView>(R.id.ratingBarText)


        var revRecommend = "yes"
        if(revRecoYes.isChecked.toString() == "true"){
            revRecommend = "Yes"
        }
        else if(revRecoNo.isChecked.toString() == "true"){
            revRecommend = "No"
        }


        revStar.setOnRatingBarChangeListener { ratingBar, fl, b ->
            ratingScale.text = fl.toString()
            when (ratingBar.rating.toInt()) {
                1 -> ratingScale.text = "Very Bad"
                2 -> ratingScale.text = "Bad"
                3 -> ratingScale.text = "Good"
                4 -> ratingScale.text = "Very Good"
                5 -> ratingScale.text = "Love It"
                else -> ratingScale.text = " "

            }

        }

        revUpdateBtn.setOnClickListener {


            if (revStar.rating.toInt() < 3) {
                Toast.makeText(
                    this@CustomerEditReviewActivity,
                    "We Are sorry for your bad experience!",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                Toast.makeText(
                    this@CustomerEditReviewActivity,
                    "Keep going, You doing Great!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            val message = revStar.rating.toString()

            val title = revTitle.text.toString()
            val description = revDescription.text.toString()
            val star = revStar.rating.toInt()

            if (title.isEmpty()) {
                revTitle.error = "Please Enter a Valid Title to your Review"
                return@setOnClickListener
            }

            if (description.isEmpty()) {
                revDescription.error = "Please Enter a Valid Description"
                return@setOnClickListener
            }

            if (revStar.rating.toInt() < 1) {
                Toast.makeText(this, "Please submit a rating", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var revRecommend = "yes"
            if (revRecoYes.isChecked.toString() == "true") {
                revRecommend = "Yes"
            } else if (revRecoNo.isChecked.toString() == "true") {
                revRecommend = "No"
            }


            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val querySnapshot = customerCollectionRef
                        .whereEqualTo("customer_email", CustomerEmail)
                        .whereEqualTo("worker_email", workerEmail)
                        .get()
                        .await()

                    for (document in querySnapshot.documents) {
                        customerCollectionRef.document(document.id)
                            .update("title", revTitle.text.toString())
                        customerCollectionRef.document(document.id)
                            .update("description", revDescription.text.toString())
                        customerCollectionRef.document(document.id)
                            .update("stars", revStar.rating.toInt())
                        customerCollectionRef.document(document.id)
                            .update("stars", revRecommend)

                    }


                } catch (e: Exception) {
                    println(e.message)
                }
            }

        }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = customerCollectionRef
                    .whereEqualTo("cusEmail", CustomerEmail)
                    .get()
                    .await()



                for (document in querySnapshot.documents) {
                    val review = document.toObject<Review>()

                    println(review?.title)
                    println(review?.description)
                    println(review?.customer_email)


                    withContext(Dispatchers.Main){
                        revTitle.setText(review?.title)
                        revDescription.setText(review?.description)

                    }

                }

            } catch (e: Exception) {
                println(e.message)
            }
        }

    }


}

