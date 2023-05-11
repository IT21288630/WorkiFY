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

class WorkerLoginActivity : AppCompatActivity() {

    private val workerCollectionRef = Firebase.firestore.collection("workers")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_login)

        val etWEmail = findViewById<EditText>(R.id.etWEmail)
        val etWPassword = findViewById<EditText>(R.id.etWPassword)
        val workerLoginBtn = findViewById<ImageView>(R.id.workerLoginBtn)
        val workerSignUp = findViewById<TextView>(R.id.workerSignUp)
        val workerResetPasswordLink = findViewById<TextView>(R.id.workerResetPasswordLink)

        workerSignUp.setOnClickListener {
            val intent = Intent(this@WorkerLoginActivity, WorkerRegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        workerResetPasswordLink.setOnClickListener {
            val intent = Intent(this@WorkerLoginActivity, WorkerResetPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }

        workerLoginBtn.setOnClickListener {
            val email = etWEmail.text.toString()
            val password = etWPassword.text.toString()

            if(email.isEmpty()){
                etWEmail.error = "Please Enter Your Email"
                return@setOnClickListener
            }
            if(password.isEmpty()){
                etWPassword.error = "Please Enter Your Password"
                return@setOnClickListener
            }

            workerLogin(email, password, etWEmail, etWPassword)
        }
    }

    public fun workerLogin(email: String, password: String, etWEmail: EditText, etWPassword: EditText) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = workerCollectionRef
                    .whereEqualTo("email", email)
                    .get()
                    .await()

                if (querySnapshot.isEmpty) {
                    withContext(Dispatchers.Main) {
                        etWEmail.error = "Wrong Email"
                    }
                }

                val querySnapshot2 = workerCollectionRef
                    .whereEqualTo("email", email)
                    .whereEqualTo("password", password)
                    .get()
                    .await()

                if (querySnapshot2.isEmpty) {
                    withContext(Dispatchers.Main) {
                        etWPassword.error = "Wrong Password"
                    }
                }

                for (document in querySnapshot2.documents) {
                    val worker = document.toObject<Worker>()

                    if (worker != null) {
                        var intent = Intent(this@WorkerLoginActivity, WorkerActivity::class.java)
                        intent.putExtra("curWorkerEmail", email)
                        startActivity(intent)
                        finish()
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@WorkerLoginActivity, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
}