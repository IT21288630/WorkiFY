package com.example.workify.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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

class ViewCusOrderDetailsActivity : AppCompatActivity() {


    private val orderCollectionRef = Firebase.firestore.collection("orders")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_cus_order_details)

        var etOrderID = findViewById<TextView>(R.id.CusOrderDetailsId)
        var etName = findViewById<EditText>(R.id.CusOrderDetailsName)
        var etAddress = findViewById<EditText>(R.id.CusOrderDetailsAddress)
        var etPhone = findViewById<EditText>(R.id.CusOrderDetailsPhone)
        var etTitle = findViewById<EditText>(R.id.CusOrderDetailsTitle)
        var etDesc = findViewById<EditText>(R.id.CusOrderDetailsDesc)
        var etDate = findViewById<EditText>(R.id.CusOrderDetailsDate)
        var etEditbtn = findViewById<Button>(R.id.CusOrderUpdate)

        var curOrderID = intent.getStringExtra("orderID")
        var curCustomerEmail = intent.getStringExtra("cusEmail")


        etEditbtn.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val querySnapshot = orderCollectionRef
                        .whereEqualTo("cusEmail", curCustomerEmail)
                        .whereEqualTo("orderID", curOrderID)
                        .get()
                        .await()

                    for (document in querySnapshot.documents) {
                        orderCollectionRef.document(document.id)
                            .update("cusName", etName.text.toString())
                        orderCollectionRef.document(document.id)
                            .update("cusAddress", etAddress.text.toString())
                        orderCollectionRef.document(document.id)
                            .update("cusPhone", etPhone.text.toString())
                        orderCollectionRef.document(document.id)
                            .update("cusTitle", etTitle.text.toString())
                        orderCollectionRef.document(document.id)
                            .update("cusDec", etDesc.text.toString())
                        orderCollectionRef.document(document.id)
                            .update("cusDate", etDate.text.toString())
                    }

                    withContext(Dispatchers.Main){
                        Toast.makeText(this@ViewCusOrderDetailsActivity, "Order details Updated", Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    println(e.message)
                }
            }
            finish()
        }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = orderCollectionRef
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
}