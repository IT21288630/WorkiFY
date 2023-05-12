package com.example.workify.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.R
import com.example.workify.activities.CustomerLoginActivity2
import com.example.workify.activities.StartActivity
import com.example.workify.activities.WorkerLoginActivity
import com.example.workify.adapters.ServicesForSettingsAdapter
import com.example.workify.dataClasses.Category
import com.example.workify.dataClasses.Customer
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


/**
 * A simple [Fragment] subclass.
 * Use the [CustomerSettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CustomerSettingFragment : Fragment(R.layout.fragment_customer_setting) {

    private val workerCollectionRef = Firebase.firestore.collection("customers")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        val email = bundle!!.getString("curCusEmail")

        val tvWorkerName = view.findViewById<TextView>(R.id.tvWorkerNameSett)
        val tvWorkerEmail = view.findViewById<TextView>(R.id.tvWorkerEmailSett)
        val etWSName = view.findViewById<EditText>(R.id.etWSName)
        val etWSEmail = view.findViewById<EditText>(R.id.etWSDistrict)
        val etWSDistrict = view.findViewById<EditText>(R.id.etWSEmail)
        val etWSPassword = view.findViewById<EditText>(R.id.etWSPassword)
        val btnWorkerEdit = view.findViewById<Button>(R.id.btnWorkerEdit)
        val workerDeleteProfileBtn = view.findViewById<Button>(R.id.workerDeleteProfileBtn)
        val tvWorkerSignOut = view.findViewById<TextView>(R.id.tvWorkerSignOut)



        tvWorkerSignOut.setOnClickListener {
            var intent = Intent(context, StartActivity::class.java)
            startActivity(intent)
        }


        workerDeleteProfileBtn.setOnClickListener {
            val builder = AlertDialog.Builder(view.context)
            builder.setTitle("Confirm!")
            builder.setMessage("Do you really want to delete your account?")

            builder.setPositiveButton("Delete") { dialog, which ->
                if (email != null) {
                    deleteProfile(email, view.context)
                }
            }

            builder.setNegativeButton("Cancel") { dialog, which ->
                dialog.cancel()
            }

            builder.show()
        }

        btnWorkerEdit.setOnClickListener {
            if (email != null) {
                updateCustomer(
                    email,
                    etWSName.text.toString(),
                    etWSDistrict.text.toString(),
                    tvWorkerName,
                    tvWorkerEmail,
                    etWSName,
                    etWSEmail,
                    etWSDistrict,
                    etWSPassword
                )
            }
        }

        if (email != null) {
            getDetails(email, tvWorkerName, tvWorkerEmail, etWSName, etWSEmail , etWSDistrict,  etWSPassword)
        }



    }

    private fun getDetails(
        email: String,
        tvWorkerName: TextView,
        tvWorkerEmail: TextView,
        etWSName: EditText,
        etWSDistrict: EditText,
        etWSEmail: EditText,
        etWSPassword: EditText
    ) {
        etWSName.text.clear()
        etWSEmail.text.clear()
        etWSDistrict.text.clear()
        etWSPassword.text.clear()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = workerCollectionRef
                    .whereEqualTo("email", email)
                    .get()
                    .await()

                for (document in querySnapshot.documents) {
                    val worker = document.toObject<Customer>()

                    if (worker != null) {
                        tvWorkerName.text = worker.name
                        tvWorkerEmail.text = worker.email
                        etWSName.hint = worker.name
                        etWSEmail.hint = worker.email
                        etWSDistrict.hint = worker.district
                        etWSPassword.hint = worker.password
                    }
                }
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    private fun updateCustomer(
        email: String,
        name: String,
        district: String,
        tvWorkerName: TextView,
        tvWorkerEmail: TextView,
        etWSName: EditText,
        etWSDistrict: EditText,
        etWEmail: EditText,
        etWSPassword: EditText
    ) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = workerCollectionRef
                    .whereEqualTo("email", email)
                    .get()
                    .await()

                for (document in querySnapshot.documents) {
                    if (name.isNotEmpty()) workerCollectionRef.document(document.id)
                        .update("name", name)
                    if (email.isNotEmpty()) workerCollectionRef.document(document.id)
                        .update("email", email)
                    if (district.isNotEmpty()) workerCollectionRef.document(document.id)
                        .update("district", district)
                    if (etWSPassword.text.toString().isNotEmpty()) workerCollectionRef.document(document.id)
                        .update("password", etWSPassword.text.toString())
                }

                getDetails(
                    email,
                    tvWorkerName,
                    tvWorkerEmail,
                    etWSName,
                    etWSDistrict,
                    etWEmail,
                    etWSPassword
                )

            } catch (e: Exception) {
                println(e.message)
            }
        }



    private fun deleteProfile(email: String, context: Context){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = workerCollectionRef
                    .whereEqualTo("email", email)
                    .get()
                    .await()

                for (document in querySnapshot.documents) {
                    workerCollectionRef.document(document.id).delete()
                }

                withContext(Dispatchers.Main) {
                    var intent = Intent(context, CustomerLoginActivity2::class.java)
                    startActivity(intent)
                }

            } catch (e: Exception) {
                println(e.message)
            }
        }
    }


}