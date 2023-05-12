package com.example.workify.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.workify.R
import com.example.workify.dataClasses.Worker
import com.example.workify.testClasses.IT21288630TestClass
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
        val etWRePassword = findViewById<EditText>(R.id.etWRePassword)
        val etWDescription = findViewById<EditText>(R.id.etWDescription)
        val etWPhone = findViewById<EditText>(R.id.etWPhone)
        val workerRegBtn = findViewById<ImageView>(R.id.workerRegBtn)
        val tvWorkerSignIn = findViewById<TextView>(R.id.tvWorkerSignIn)

        tvWorkerSignIn.setOnClickListener {
            val intent = Intent(this@WorkerRegisterActivity, WorkerLoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        workerRegBtn.setOnClickListener {
            val name = etWName.text.toString()
            val email = etWEmail.text.toString()
            val district = etWDistrict.text.toString()
            val password = etWPassword.text.toString()
            val rePassword = etWRePassword.text.toString()
            val description = etWDescription.text.toString()
            val phone = etWPhone.text.toString()

            val result = IT21288630TestClass.register(name, email, district, password, rePassword, description, phone)

            when (result){
                "name" -> {
                    etWName.error = "Please Enter Your Name"
                    return@setOnClickListener
                }

                "email" -> {
                    etWEmail.error = "Please Enter a Valid Email"
                    return@setOnClickListener
                }

                "emailEmp" -> {
                    etWEmail.error = "Please Enter Your Email"
                    return@setOnClickListener
                }

                "phone" -> {
                    etWPhone.error = "Please Enter a Valid PhoneNumber"
                    return@setOnClickListener
                }

                "phoneEmp" -> {
                    etWPhone.error = "Please Enter Your PhoneNumber"
                    return@setOnClickListener
                }

                "disc" -> {
                    etWDistrict.error = "Please Enter Your District"
                    return@setOnClickListener
                }

                "desc" -> {
                    etWDescription.error = "Please Enter Your Description"
                    return@setOnClickListener
                }

                "repass" -> {
                    etWRePassword.error = "Password and the Confirm Password Should Match"
                    return@setOnClickListener
                }

                "repassEmp" -> {
                    etWRePassword.error = "Please Enter the Confirm Password"
                    return@setOnClickListener
                }

                "password" -> {
                    etWPassword.error = "Please Enter a Password"
                    return@setOnClickListener
                }

            }

            val worker = Worker(name, email, district, password, description, 0.0, null, phone)

            saveWorker(worker)
        }


    }

    private fun saveWorker(worker: Worker) = CoroutineScope(Dispatchers.IO).launch {
        try {
            workerCollectionRef.add(worker).await()

            withContext(Dispatchers.Main) {
                Toast.makeText(this@WorkerRegisterActivity, "Successfully Registered", Toast.LENGTH_LONG).show()
                var intent = Intent(this@WorkerRegisterActivity, WorkerLoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@WorkerRegisterActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}