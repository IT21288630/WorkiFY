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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class WorkerResetPasswordActivity : AppCompatActivity() {

    private val workerCollectionRef = Firebase.firestore.collection("workers")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_reset_password)

        val etWorkerResetEmail = findViewById<EditText>(R.id.etWorkerResetEmail)
        val etWorkerResetPassword = findViewById<EditText>(R.id.etWorkerResetPassword)
        val ivWorkerResetPasswordBtn = findViewById<ImageView>(R.id.ivWorkerResetPasswordBtn)
        val tvWorkerSignInInsteadLink = findViewById<TextView>(R.id.tvWorkerSignInInsteadLink)

        tvWorkerSignInInsteadLink.setOnClickListener {
            val intent = Intent(
                this@WorkerResetPasswordActivity,
                WorkerLoginActivity::class.java
            )
            startActivity(intent)
        }

        ivWorkerResetPasswordBtn.setOnClickListener {
            if (etWorkerResetEmail.text.toString().isEmpty()) {
                etWorkerResetEmail.error = "Please Enter Your Email"
                return@setOnClickListener
            }
            if (etWorkerResetPassword.text.toString().isEmpty()) {
                etWorkerResetPassword.error = "Please Enter a New Password"
                return@setOnClickListener
            }


            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val querySnapshot = workerCollectionRef
                        .whereEqualTo("email", etWorkerResetEmail.text.toString())
                        .get()
                        .await()

                    if (querySnapshot.isEmpty) {
                        withContext(Dispatchers.Main) {
                            etWorkerResetEmail.error = "Email does not exist"
                        }
                        return@launch
                    }

                    for (document in querySnapshot.documents) {
                        workerCollectionRef.document(document.id)
                            .update("password", etWorkerResetPassword.text.toString())
                    }

                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@WorkerResetPasswordActivity,
                            "Password Reset is Successful",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent = Intent(
                            this@WorkerResetPasswordActivity,
                            WorkerLoginActivity::class.java
                        )
                        startActivity(intent)
                    }

                } catch (e: Exception) {
                    println(e.message)
                }
            }
        }
    }
}