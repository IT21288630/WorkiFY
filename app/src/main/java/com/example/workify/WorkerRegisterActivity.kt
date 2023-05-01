package com.example.workify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
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
        val workerRegBtn = findViewById<ImageView>(R.id.workerRegBtn)

        workerRegBtn.setOnClickListener {
            val name = etWName.text.toString()
            val email = etWEmail.text.toString()
            val district = etWDistrict.text.toString()
            val password = etWPassword.text.toString()
            val description = etWDescription.text.toString()

            val worker = Worker(name, email, district, password, description)

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