package com.example.workify.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.*
import com.example.workify.R

import com.example.workify.dataClasses.Review
import com.google.firebase.firestore.ktx.firestore
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

        var revRecoYes = findViewById<RadioButton>(R.id.radioRecYes)
        var revRecoNo = findViewById<RadioButton>(R.id.radioRecNo)

        var revAddBtn = findViewById<Button>(R.id.cusRevSubBtn)
        var laterBtn = findViewById<Button>(R.id.laterBtn)
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

            if(revStar.rating.toInt() == 1){
                Toast.makeText(this@CustomerReviewActivity, "We Are sorry for your bad experience with this seller", Toast.LENGTH_LONG).show()

            }
            else if(revStar.rating.toInt() == 2){
                Toast.makeText(this@CustomerReviewActivity, "Hope you got your work done", Toast.LENGTH_SHORT).show()

            }


            else{
                Toast.makeText(this@CustomerReviewActivity, "Try our other services too", Toast.LENGTH_SHORT).show()
            }

        }
        laterBtn.setOnClickListener{
            val intent = Intent(this@CustomerReviewActivity, CustomerActivity::class.java)
            startActivity(intent)
        }


        revAddBtn.setOnClickListener {

            val title = revTitle.text.toString()
            val description = revDescription.text.toString()
            val star = revStar.rating.toInt()

            if(title.isEmpty()){
                revTitle.error = "Please Enter a Valid Title to your Review"
                return@setOnClickListener
            }

            if(description.isEmpty()){
                revDescription.error = "Please Enter a Valid Description"
                return@setOnClickListener
            }

            if(revStar.rating.toInt() < 1){
                Toast.makeText(this, "Please submit a rating", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val message = revStar.rating.toString()


            var revRecommend = "yes"
            if(revRecoYes.isChecked.toString() == "true"){
                revRecommend = "Yes"
            }
            else if(revRecoNo.isChecked.toString() == "true"){
                revRecommend = "No"
            }

            var revId = getRandomString()
            var review = Review(
                revTitle.text.toString(), revRecommend, revDescription.text.toString(), revStar.rating.toInt(),
                workerEmail.toString(), curCustomerEmail.toString(), revId
            )

            CoroutineScope(Dispatchers.IO).launch {


                println("this is the coroutinescope dispatcher")
                try {
                    val querySnapshot = ReviewCollectionRef
                        .add(review)
                        .await()

                    withContext(Dispatchers.Main){
                        Toast.makeText(this@CustomerReviewActivity, "Review Successfully Added!", Toast.LENGTH_SHORT).show()

                    }

                } catch (e: Exception) {

                    println(e.message)
                }
            }
        }


    }
    fun getRandomString() : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..5)
            .map { allowedChars.random() }
            .joinToString("")
    }

}