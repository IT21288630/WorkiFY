package com.example.workify

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.adapters.ServicesForHomeAdapter
import com.example.workify.adapters.ServicesForSettingsAdapter
import com.example.workify.dataClasses.Category
import com.example.workify.dataClasses.Worker
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
        val etWSName = view.findViewById<TextView>(R.id.etWSName)
        val etWSDistrict = view.findViewById<TextView>(R.id.etWSDistrict)
        val etWSDescription = view.findViewById<TextView>(R.id.etWSDescription)
        val btnWorkerEdit = view.findViewById<TextView>(R.id.btnWorkerEdit)
        val ivAddServiceBtn = view.findViewById<ImageView>(R.id.ivAddServiceBtn)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvSettingsServices)

        ivAddServiceBtn.setOnClickListener {
            if (email != null) {
                displayDialog(view.context, email, recyclerView)
            }
        }

        btnWorkerEdit.setOnClickListener {
            if (email != null) {
                updateWorker(
                    email,
                    etWSName.text.toString(),
                    etWSDescription.text.toString(),
                    etWSDistrict.text.toString()
                )
            }
        }

        workerCollectionRef.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            firebaseFirestoreException?.let {
                println(it.message)
                return@addSnapshotListener
            }
            querySnapshot?.let {
                for (document in it) {
                    val worker = document.toObject<Worker>()

                    if (worker != null) {
                        tvWorkerName.text = worker.name
                        tvWorkerEmail.text = worker.email
                        etWSName.hint = worker.name
                        etWSDistrict.hint = worker.district
                        etWSDescription.hint = worker.description

                    }
                }
            }
        }


        if (email != null) {
            getServices(recyclerView, view.context, email)
        }
    }

    private fun updateWorker(email: String, name: String, description: String, district: String) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = workerCollectionRef
                    .whereEqualTo("email", email)
                    .get()
                    .await()

                for (document in querySnapshot.documents) {
                    val worker = document.toObject<Worker>()

                    if (name.isNotEmpty()) workerCollectionRef.document(document.id)
                        .update("name", name)
                    if (description.isNotEmpty()) workerCollectionRef.document(document.id)
                        .update("description", description)
                    if (district.isNotEmpty()) workerCollectionRef.document(document.id)
                        .update("district", district)
                }

            } catch (e: Exception) {
                println(e.message)
            }
        }

    private fun getServices(recyclerView: RecyclerView, context: Context, email: String){
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
        builder.setPositiveButton("ADD") { dialog, which ->

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

    fun displayEditServiceDialog(context: Context, service: String, description: String, rate: String) {
        println("Im here")

        // Create a new instance of AlertDialog.Builder
        val builder = AlertDialog.Builder(context)
        val inflator = layoutInflater
        val view: View = inflator.inflate(R.layout.edit_service_dialog, null)

        // Set the view
        builder.setView(view)

        val tvEditServiceName = view.findViewById<TextView>(R.id.tvEditServiceName)
        val etEditServiceDesc = view.findViewById<EditText>(R.id.etEditServiceDesc)
        val etEditServiceHrRate = view.findViewById<EditText>(R.id.etEditServiceHrRate)

        tvEditServiceName.text = service
        etEditServiceDesc.setText(description)
        etEditServiceHrRate.setText(rate)

        // Set the positive button action
        builder.setPositiveButton("ADD") { dialog, which ->


        }

        // Set the negative button action
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.cancel()
        }

        // Create and show the alert dialog
        val alertDialog = builder.create()
        alertDialog.show()
    }

}