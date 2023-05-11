package com.example.workify.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workify.R
import com.example.workify.adapters.WorkerPendingAdapter
import com.example.workify.dataClasses.Order
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ViewOrderDetailsActivity : AppCompatActivity() {


    private val orderCollectionRef = Firebase.firestore.collection("orders")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_order_details)

        var etOrderID = findViewById<TextView>(R.id.OrderDetailsId)
        var etName = findViewById<TextView>(R.id.OrderDetailsName)
        var etAddress = findViewById<TextView>(R.id.OrderDetailsAddress)
        var etPhone = findViewById<TextView>(R.id.OrderDetailsPhone)
        var etTitle = findViewById<TextView>(R.id.OrderDetailsTitle)
        var etDesc = findViewById<TextView>(R.id.OrderDetailsDesc)
        var etDate = findViewById<TextView>(R.id.OrderDetailsDate)
        var etDonebtn = findViewById<Button>(R.id.OrderConfirm)


        var curOrderID = intent.getStringExtra("orderID")
        var curWorkerEmail = intent.getStringExtra("workEmail")

        println(curOrderID)
        println(curWorkerEmail)

        etDonebtn.setOnClickListener {

            finish();

        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = orderCollectionRef
                    .whereEqualTo("workEmail", curWorkerEmail)
                    .whereEqualTo("orderID", curOrderID)
                    .get()
                    .await()



                for (document in querySnapshot.documents) {
                    val order = document.toObject<Order>()

                    println(order?.cusName)
                    println(order?.cusAddress)
                    println(order?.cusPhone)
                    println(order?.cusTitle)
                    println(order?.cusDesc)
                    println(order?.cusDate)

                    if(order != null){
                        withContext(Dispatchers.Main){
                            etOrderID.text = order.orderID
                            etName.text = order.cusName
                            etAddress.text = order.cusAddress
                            etPhone.text = order.cusPhone
                            etTitle.text = order.cusTitle
                            etDesc.text = order.cusDesc
                            etDate.text = order.cusDate
                        }
                    }


                }

            } catch (e: Exception) {
                println(e.message)
            }
        }
    }
}