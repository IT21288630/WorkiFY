package com.example.workify.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.workify.R
import com.example.workify.dataClasses.Worker
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class WorkerRegisterActivity : AppCompatActivity() {

    private val workerCollectionRef = Firebase.firestore.collection("workers")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_register)

        val etWName = findViewById<EditText>(R.id.etWName)
        val etWEmail = findViewById<EditText>(R.id.etWEmail)
        val etWDistrict = findViewById<EditText>(R.id.etWDistrict)
        val etWPassword = findViewById<EditText>(R.id.etWPassword)
        val etWDescription = findViewById<EditText>(R.id.etWDescription)
        val etWPhone = findViewById<EditText>(R.id.etWPhone)
        val workerRegBtn = findViewById<ImageView>(R.id.workerRegBtn)

        workerRegBtn.setOnClickListener {
            val name = etWName.text.toString()
            val email = etWEmail.text.toString()
            val district = etWDistrict.text.toString()
            val password = etWPassword.text.toString()
            val description = etWDescription.text.toString()
            val phone = etWPhone.text.toString()

            if(name.isEmpty()){
                etWName.error = "Please Enter Your Name"
                return@setOnClickListener
            }
            if(email.isEmpty()){
                etWEmail.error = "Please Enter Your Email"
                return@setOnClickListener
            }
            if(phone.isEmpty()){
                etWPhone.error = "Please Enter Your PhoneNumber"
                return@setOnClickListener
            }
            if(phone.length != 10){
                etWPhone.error = "Please Enter a Valid PhoneNumber"
                return@setOnClickListener
            }
            try {
                var tempPhone = phone.toInt()
            }catch (e: Exception){
                etWPhone.error = "Please Enter a Valid PhoneNumber"
                return@setOnClickListener
            }
            if(district.isEmpty()){
                etWDistrict.error = "Please Enter Your District"
                return@setOnClickListener
            }
            if(description.isEmpty()){
                etWDescription.error = "Please Enter Your Description"
                return@setOnClickListener
            }
            if(password.isEmpty()){
                etWPassword.error = "Please Enter a Password"
                return@setOnClickListener
            }


            val worker = Worker(name, email, district, password, description, 0.0, null, phone)

            saveWorker(worker)
        }


    }

    private fun saveWorker(worker: Worker) = CoroutineScope(Dispatchers.IO).launch {
        try {
            workerCollectionRef.add(worker).await()

            withContext(Dispatchers.Main) {
                Toast.makeText(this@WorkerRegisterActivity, "Worker added", Toast.LENGTH_LONG).show()
            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@WorkerRegisterActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}