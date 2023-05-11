package com.example.workify.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.workify.R
import com.example.workify.dataClasses.Customer
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CustomerSignupActivity : AppCompatActivity() {

    private val workerCollectionRef = Firebase.firestore.collection("customers")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_signup)

        val etWName = findViewById<EditText>(R.id.etWName)
        val etWEmail = findViewById<EditText>(R.id.etWEmail)
        val etWDistrict = findViewById<EditText>(R.id.etWDistrict)
        val etWPassword = findViewById<EditText>(R.id.etWPassword)
        val workerRegBtn = findViewById<ImageView>(R.id.workerRegBtn)

        workerRegBtn.setOnClickListener {
            val name = etWName.text.toString()
            val email = etWEmail.text.toString()
            val district = etWDistrict.text.toString()
            val password = etWPassword.text.toString()

            if(name.isEmpty()){
                etWName.error = "Please Enter Your Name"
                return@setOnClickListener
            }
            if(email.isEmpty()){
                etWEmail.error = "Please Enter Your Email"
                return@setOnClickListener
            }
            if(district.isEmpty()){
                etWDistrict.error = "Please Enter Your District"
                return@setOnClickListener
            }
            if(password.isEmpty()){
                etWPassword.error = "Please Enter a Password"
                return@setOnClickListener
            }


            val customer = Customer(name, email, district, password)

            saveCustomer(customer)
        }


    }

    private fun saveCustomer(worker: Customer) = CoroutineScope(Dispatchers.IO).launch {
        try {
            workerCollectionRef.add(worker).await()

            withContext(Dispatchers.Main) {
                Toast.makeText(this@CustomerSignupActivity, "Customer added", Toast.LENGTH_LONG).show()
                var intent = Intent(this@CustomerSignupActivity, CustomerLoginActivity2::class.java)
                startActivity(intent)
                finish()
            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@CustomerSignupActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}