package com.example.workify.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.workify.R
import com.example.workify.dataClasses.Order
import com.example.workify.dataClasses.Review
import com.example.workify.dataClasses.Worker
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CustomerEditReviewActivity : AppCompatActivity() {

    private val customerCollectionRef = Firebase.firestore.collection("customer_reviews")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cutomer_edit_review)


        var revTitle = findViewById<EditText>(R.id.cutomerRevTitle)
        var revStar = findViewById<RatingBar>(R.id.RatingBar)
        var revDescription = findViewById<EditText>(R.id.addcustomRevDescription)

        var revRecoYes = findViewById<RadioButton>(R.id.radioRecYes)
        var revRecoNo = findViewById<RadioButton>(R.id.radioRecNo)

        var revUpdateBtn = findViewById<Button>(R.id.editReviewUpdateBtn)
        var revCancelBtn = findViewById<Button>(R.id.editReviewCancelBtn)

        val ratingScale = findViewById<TextView>(R.id.ratingBarText)

        var curCustomerEmail = intent.getStringExtra("cusEmail")
        var workerEmail = intent.getStringExtra("workerEmail")


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


            if(revStar.rating.toInt() < 3){
                Toast.makeText(this@CustomerEditReviewActivity, "We Are sorry for your bad experience!", Toast.LENGTH_SHORT).show()

            }
            else{
                Toast.makeText(this@CustomerEditReviewActivity, "Keep going, You doing Great!", Toast.LENGTH_SHORT).show()
            }

            val message = revStar.rating.toString()

            val title = revTitle.text.toString()
            val description = revDescription.text.toString()
            val star = revStar.rating.toInt()





            CoroutineScope(Dispatchers.IO).launch {

                println("this is the coroutinescope dispatcher")
                try {
                    val querySnapshot = customerCollectionRef
                        .whereEqualTo("customer_email", curCustomerEmail)
                        .get()
                        .await()


                    for (document in querySnapshot.documents) {
                        customerCollectionRef.document(document.id)
                            .update("Title", revTitle.text.toString())
                        customerCollectionRef.document(document.id)
                            .update("Description", revDescription.text.toString())
                        customerCollectionRef.document(document.id)
                            .update("Star", revStar.rating.toInt())
                        customerCollectionRef.document(document.id)
                            .update("Recommend", revRecommend)

                    }

                } catch (e: Exception) {
                    println(e.message)
                }
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = customerCollectionRef
                    .whereEqualTo("customer_email", curCustomerEmail)
                    .whereEqualTo("worker_email", workerEmail)
                    .get()
                    .await()



                for (document in querySnapshot.documents) {
                    val review = document.toObject<Review>()

                    println(review?.title)
                    println(review?.customer_email)
                    println(review?.description)
                    println(review?.stars)
                    println(review?.recomment)
                    println(review?.worker_email)

                    withContext(Dispatchers.Main){

                        revTitle.setText(review?.title)
                        revDescription.setText(review?.description)
                        revStar.setNumStars((review?.stars!!))
                        // revRecommend.setText(review?.recomment)
                        // etDesc.setText(review?.cusDesc)
                        //  etDate.setText(review?.cusDesc)
                    }

                }

            } catch (e: Exception) {
                println(e.message)
            }
        }


    }

}

