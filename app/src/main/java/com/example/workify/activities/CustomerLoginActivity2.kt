package com.example.workify.activities

import android.app.Person
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

class CustomerLoginActivity2 : AppCompatActivity() {

    private val workerCollectionRef = Firebase.firestore.collection("customers")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_login2)

        val etWEmail = findViewById<EditText>(R.id.etWEmail)
        val etWPassword = findViewById<EditText>(R.id.etWPassword)
        val workerLoginBtn = findViewById<ImageView>(R.id.workerLoginBtn)
        val workerSignUp = findViewById<TextView>(R.id.workerSignUp)
        val workerResetPasswordLink = findViewById<TextView>(R.id.workerResetPasswordLink)

        workerSignUp.setOnClickListener {
            val intent = Intent(this@CustomerLoginActivity2, CustomerSignupActivity::class.java)
            startActivity(intent)
        }

        workerResetPasswordLink.setOnClickListener {
            val intent = Intent(this@CustomerLoginActivity2, CustomerResetPasswordActivity::class.java)
            startActivity(intent)
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

            customerLogin(email, password)
        }
    }

    private fun customerLogin(email: String, password: String) =
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
                            this@CustomerLoginActivity2,
                            "Wrong credentials",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                for (document in querySnapshot.documents) {
                    val worker = document.toObject<Worker>()

                    if (worker != null) {
                        var intent = Intent(this@CustomerLoginActivity2, CustomerActivity::class.java)
                        intent.putExtra("curCusEmail", email)
                        startActivity(intent)
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CustomerLoginActivity2, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
}