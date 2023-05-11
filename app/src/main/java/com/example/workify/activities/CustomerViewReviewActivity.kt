package com.example.workify.activities
/*
import android.os.Bundle
import android.widget.*
import com.example.workify.R
import com.example.workify.dataClasses.Order
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CustomerViewReviewActivity {
    private val revCollectionRef = Firebase.firestore.collection("customer_reviews")

     fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_rev_cus)

        var revTitle = findViewById<EditText>(R.id.cutomerRevTitle)
        var revStar = findViewById<RatingBar>(R.id.RatingBar)
        var revDescription = findViewById<EditText>(R.id.addcustomRevDescription)

        var revRecoYes = findViewById<RadioButton>(R.id.radioRecYes)
        var revRecoNo = findViewById<RadioButton>(R.id.radioRecNo)

        var revAddBtn = findViewById<Button>(R.id.cusRevSubBtn)
        val ratingScale = findViewById<TextView>(R.id.ratingBarText)


        var curOrderID = intent.getStringExtra("orderID")
        var curCustomerEmail = intent.getStringExtra("cusEmail")


        etEditbtn.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val querySnapshot = revCollectionRef
                        .whereEqualTo("cusEmail", curCustomerEmail)
                        .whereEqualTo("orderID", curOrderID)
                        .get()
                        .await()

                    for (document in querySnapshot.documents) {
                        revCollectionRef.document(document.id)
                            .update("cusName", etName.text.toString())
                        revCollectionRef.document(document.id)
                            .update("cusAddress", etAddress.text.toString())
                        revCollectionRef.document(document.id)
                            .update("cusPhone", etPhone.text.toString())
                        revCollectionRef.document(document.id)
                            .update("cusTitle", etTitle.text.toString())
                        revCollectionRef.document(document.id)
                            .update("cusDec", etDesc.text.toString())
                        revCollectionRef.document(document.id)
                            .update("cusDate", etDate.text.toString())
                    }


                } catch (e: Exception) {
                    println(e.message)
                }
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = revCollectionRef
                    .whereEqualTo("cusEmail", curCustomerEmail)
                    .whereEqualTo("orderID", curOrderID)
                    .get()
                    .await()



                for (document in querySnapshot.documents) {
                    val order = document.toObject<Order>()

                    println(order?.cusName)
                    println(order?.cusAddress)
                    println(order?.cusPhone)
                    println(order?.cusName)
                    println(order?.cusName)
                    println(order?.cusName)

                    withContext(Dispatchers.Main){
                        etOrderID.text = order?.orderID
                        etName.setText(order?.cusName)
                        etAddress.setText(order?.cusAddress)
                        etPhone.setText(order?.cusPhone)
                        etTitle.setText(order?.cusTitle)
                        etDesc.setText(order?.cusDesc)
                        etDate.setText(order?.cusDesc)
                    }

                }

            } catch (e: Exception) {
                println(e.message)
            }
        }
    }
}*/