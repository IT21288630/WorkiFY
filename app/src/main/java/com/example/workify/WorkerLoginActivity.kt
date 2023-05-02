package com.example.workify

import android.app.Person
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.workify.dataClasses.Worker
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class WorkerLoginActivity : AppCompatActivity() {

    private val workerCollectionRef = Firebase.firestore.collection("workers")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_login)

        val etWEmail = findViewById<EditText>(R.id.etWEmail)
        val etWPassword = findViewById<EditText>(R.id.etWPassword)
        val workerLoginBtn = findViewById<ImageView>(R.id.workerLoginBtn)
        val workerSignUp = findViewById<TextView>(R.id.workerSignUp)

        workerSignUp.setOnClickListener {
            val intent = Intent(this@WorkerLoginActivity, WorkerRegisterActivity::class.java)
            startActivity(intent)
        }

        workerLoginBtn.setOnClickListener {
            val email = etWEmail.text.toString()
            val password = etWPassword.text.toString()

            workerLogin(email, password)
        }
    }

    private fun workerLogin(email: String, password: String) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = workerCollectionRef
                    .whereEqualTo("email", email)
                    .whereEqualTo("password", password)
                    .get()
                    .await()

                if (querySnapshot.isEmpty) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@WorkerLoginActivity,
                            "Wrong credentials",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                for (document in querySnapshot.documents) {
                    val worker = document.toObject<Worker>()

                    if (worker != null) {
                        var intent = Intent(this@WorkerLoginActivity, WorkerActivity::class.java)
                        intent.putExtra("curWorkerEmail", email)
                        startActivity(intent)
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@WorkerLoginActivity, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
}