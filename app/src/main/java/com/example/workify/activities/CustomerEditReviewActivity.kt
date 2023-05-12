package com.example.workify.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.workify.R
import com.example.workify.dataClasses.Review
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CustomerEditReviewActivity : AppCompatActivity() {


    private lateinit var revTitle: EditText
    private lateinit var revDescription : EditText
    private lateinit var revStar : RatingBar


    private val customerCollectionRef = Firebase.firestore.collection("customer_reviews")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_edit_review)

        var CustomerEmail = intent.getStringExtra("customer_email")
        var id =intent.getStringExtra("rev_ID")

        println("This is the id in the begning: "+ id)

        revTitle = findViewById(R.id.cutomerRevTitle)
        revStar = findViewById(R.id.EditRatingBar)
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
                        .whereEqualTo("rev_ID", id)
                        .get()
                        .await()

                    println("This is the id in the 1st coro: "+ id)
                    for (document in querySnapshot.documents) {
                        customerCollectionRef.document(document.id)
                            .update("title", revTitle.text.toString())
                        customerCollectionRef.document(document.id)
                            .update("description", revDescription.text.toString())
                        customerCollectionRef.document(document.id)
                            .update("recomment", revRecommend)
                        customerCollectionRef.document(document.id)
                            .update("stars", revStar)



                        withContext(Dispatchers.Main){
                            Toast.makeText(
                                this@CustomerEditReviewActivity,
                                "Updated!",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                    }

                } catch (e: Exception) {
                    println(e.message)
                }
            }




        }


        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = customerCollectionRef
                    .whereEqualTo("customer_email", CustomerEmail)
                    .whereEqualTo("rev_ID", id)
                    .get()
                    .await()

                println("Retrieving")
                println(CustomerEmail + id)

                for (document in querySnapshot.documents) {

                    println("Review title : "+document.get("title").toString())

                    revTitle.setText(document.get("title").toString())
                    revDescription.setText(document.get("description").toString())

                }


            } catch (e: Exception) {
                println(e.message)
            }
        }

        revCancelBtn.setOnClickListener {
            val intent = Intent(this@CustomerEditReviewActivity, CustomerActivity::class.java)
            startActivity(intent)

        }

    }


}

