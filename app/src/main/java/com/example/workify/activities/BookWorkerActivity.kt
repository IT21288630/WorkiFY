package com.example.workify.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.workify.R
import com.example.workify.dataClasses.Order
import com.example.workify.testClasses.IT21256646TestClass
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class BookWorkerActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etAddress: EditText
    private lateinit var etPhone: EditText
    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var etDate: EditText
    private lateinit var WorkerDisplayName:TextView
    private lateinit var btnBookWorker: Button


    private val orderCollectionRef = Firebase.firestore.collection("orders")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_worker)

        val workerEmail = intent.getStringExtra("workerEmail")
        val cusEmail = intent.getStringExtra("cusEmail")

        etName = findViewById(R.id.cusName)
        etAddress = findViewById(R.id.cusAddress)
        etPhone = findViewById(R.id.cusPhone)
        etTitle = findViewById(R.id.cusTitle)
        etDescription = findViewById(R.id.cusDesc)
        etDate = findViewById(R.id.cusDate)
        btnBookWorker = findViewById(R.id.bookWorkerbtn)

        btnBookWorker.setOnClickListener {

            val cusName = etName.text.toString()
            val cusAddress = etAddress.text.toString()
            val cusPhone = etPhone.text.toString()
            val cusTitle = etTitle.text.toString()
            val cusDesc = etDescription.text.toString()
            val cusDate = etDate.text.toString()
            val orderID = getRandomString()

            IT21256646TestClass.DetailsEmptyCheck(cusName,cusAddress,cusPhone,cusTitle,cusDesc,cusDesc)

            IT21256646TestClass.ValideData(cusPhone)

            val order = Order(cusName,cusAddress,cusPhone,cusTitle,cusDesc,cusDate,cusEmail,intent.getStringExtra("workerEmail"),"Pending",orderID)
            orderWorker(order)

            finish()
        }
    }

    private fun orderWorker(order: Order) = CoroutineScope(Dispatchers.IO).launch {
        try {
            orderCollectionRef.add(order).await()

            withContext(Dispatchers.Main) {
                Toast.makeText(this@BookWorkerActivity, "Order added", Toast.LENGTH_LONG).show()
            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@BookWorkerActivity, e.message, Toast.LENGTH_LONG).show()
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
