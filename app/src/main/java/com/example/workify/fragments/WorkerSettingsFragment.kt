package com.example.workify.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.R
import com.example.workify.activities.StartActivity
import com.example.workify.activities.WorkerLoginActivity
import com.example.workify.adapters.ServicesForSettingsAdapter
import com.example.workify.dataClasses.Category
import com.example.workify.dataClasses.Worker
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
 */
class WorkerSettingsFragment : Fragment(R.layout.fragment_worker_settings) {

    private val workerCollectionRef = Firebase.firestore.collection("workers")
    private val workerCatCollectionRef = Firebase.firestore.collection("worker_cat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        val email = bundle!!.getString("curWorkerEmail")

        val tvWorkerName = view.findViewById<TextView>(R.id.tvWorkerNameSett)
        val tvWorkerEmail = view.findViewById<TextView>(R.id.tvWorkerEmailSett)
        val etWSName = view.findViewById<EditText>(R.id.etWSName)
        val etWSDistrict = view.findViewById<EditText>(R.id.etWSDistrict)
        val etWSDescription = view.findViewById<EditText>(R.id.etWSDescription)
        val etWSPhone = view.findViewById<EditText>(R.id.etWSPhone)
        val btnWorkerEdit = view.findViewById<Button>(R.id.btnWorkerEdit)
        val ivAddServiceBtn = view.findViewById<ImageView>(R.id.ivAddServiceBtn)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvSettingsServices)
        val workerDeleteProfileBtn = view.findViewById<Button>(R.id.workerDeleteProfileBtn)
        val tvWorkerSignOut = view.findViewById<TextView>(R.id.tvWorkerSignOut)

        ivAddServiceBtn.setOnClickListener {
            if (email != null) {
                displayDialog(view.context, email, recyclerView)
            }
        }

        tvWorkerSignOut.setOnClickListener {
            if (email != null) {
                workerSignOut(email)
            }
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
                updateWorker(
                    email,
                    etWSName.text.toString(),
                    etWSDescription.text.toString(),
                    etWSDistrict.text.toString(),
                    tvWorkerName,
                    tvWorkerEmail,
                    etWSName,
                    etWSDistrict,
                    etWSDescription,
                    etWSPhone
                )
            }
        }

        if (email != null) {
            getDetails(email, tvWorkerName, tvWorkerEmail, etWSName, etWSDistrict, etWSDescription, etWSPhone)
        }


        if (email != null) {
            getServices(recyclerView, view.context, email)
        }
    }

    private fun getDetails(
        email: String,
        tvWorkerName: TextView,
        tvWorkerEmail: TextView,
        etWSName: EditText,
        etWSDistrict: EditText,
        etWSDescription: EditText,
        etWSPhone: EditText
    ) {
        etWSName.text.clear()
        etWSDistrict.text.clear()
        etWSDescription.text.clear()
        etWSPhone.text.clear()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = workerCollectionRef
                    .whereEqualTo("email", email)
                    .get()
                    .await()

                for (document in querySnapshot.documents) {
                    val worker = document.toObject<Worker>()

                    if (worker != null) {
                        tvWorkerName.text = worker.name
                        tvWorkerEmail.text = worker.email
                        etWSName.hint = worker.name
                        etWSDistrict.hint = worker.district
                        etWSDescription.hint = worker.description
                        etWSPhone.hint = worker.phone
                    }
                }
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    private fun updateWorker(
        email: String,
        name: String,
        description: String,
        district: String,
        tvWorkerName: TextView,
        tvWorkerEmail: TextView,
        etWSName: EditText,
        etWSDistrict: EditText,
        etWSDescription: EditText,
        etWSPhone: EditText
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
                    if (description.isNotEmpty()) workerCollectionRef.document(document.id)
                        .update("description", description)
                    if (district.isNotEmpty()) workerCollectionRef.document(document.id)
                        .update("district", district)
                    if (etWSPhone.text.toString().isNotEmpty()) workerCollectionRef.document(document.id)
                        .update("phone", etWSPhone.text.toString())
                }

                getDetails(
                    email,
                    tvWorkerName,
                    tvWorkerEmail,
                    etWSName,
                    etWSDistrict,
                    etWSDescription,
                    etWSPhone
                )

            } catch (e: Exception) {
                println(e.message)
            }
        }

    private fun getServices(recyclerView: RecyclerView, context: Context, email: String) {
        val categories = mutableListOf<Category>()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = workerCatCollectionRef
                    .whereEqualTo("wEmail", email)
                    .get()
                    .await()

                for (document in querySnapshot.documents) {
                    val name = document.get("cName").toString()
                    val rate = document.get("hrRate").toString()
                    val description = document.get("wDesc").toString()

                    val category = Category(name, description, "", rate)

                    categories.add(category)
                }

                withContext(Dispatchers.Main) {
                    val adapter = ServicesForSettingsAdapter(categories, context, email)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    adapter.setData(categories, context)
                }


            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    fun displayDialog(context: Context, email: String, recyclerView: RecyclerView) {
        // Create a new instance of AlertDialog.Builder
        val builder = AlertDialog.Builder(context)
        val inflator = layoutInflater
        val view: View = inflator.inflate(R.layout.add_service_dialog, null)

        // Set the view
        builder.setView(view)

        val spAddServiceSelect = view.findViewById<Spinner>(R.id.spAddServiceSelect)
        val etAddServiceDesc = view.findViewById<EditText>(R.id.etAddServiceDesc)
        val etAddServiceHrRate = view.findViewById<EditText>(R.id.etAddServiceHrRate)

        var service: String? = null

        spAddServiceSelect.onItemSelectedListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                service = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                TODO("Not yet implemented")
            }

        }

        // Set the positive button action
        builder.setPositiveButton("Add") { dialog, which ->

            val map = mutableMapOf<String, Any>()
            val description = etAddServiceDesc.text.toString()
            val rate = etAddServiceHrRate.text.toString()

            map["hrRate"] = rate
            map["wDesc"] = description
            map["wEmail"] = email
            map["cName"] = service!!

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    workerCatCollectionRef.add(map).await()

                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Service added", Toast.LENGTH_LONG).show()
                    }

                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }

            getServices(recyclerView, context, email)
        }

        // Set the negative button action
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.cancel()
        }

        // Create and show the alert dialog
        val alertDialog = builder.create()
        alertDialog.show()
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
                    var intent = Intent(context, WorkerLoginActivity::class.java)
                    startActivity(intent)
                }

            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    private fun workerSignOut(email: String){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = workerCollectionRef
                    .whereEqualTo("email", email)
                    .get()
                    .await()

                for (document in querySnapshot.documents) {
                    workerCollectionRef.document(document.id).update("fcmToken", FieldValue.delete())
                }

                withContext(Dispatchers.Main) {
                    var intent = Intent(context, StartActivity::class.java)
                    startActivity(intent)
                }

            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

}