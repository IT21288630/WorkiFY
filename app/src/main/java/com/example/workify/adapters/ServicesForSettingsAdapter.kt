package com.example.workify.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.*
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

class ServicesForSettingsAdapter(
    private var data: List<Category>,
    private var context: Context,
    private var email: String
) :
    RecyclerView.Adapter<ServicesForSettingsAdapter.ViewHolder>() {

    private val workerCatCollectionRef = Firebase.firestore.collection("worker_cat")

    inner class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val tvSettingsServiceName: TextView
        val ivSettingsServiceEditBtn: ImageView
        val ivSettingsServiceDeleteBtn: ImageView


        init {
            tvSettingsServiceName = view.findViewById(R.id.tvSettingsServiceName)
            ivSettingsServiceEditBtn = view.findViewById(R.id.ivSettingsServiceEditBtn)
            ivSettingsServiceDeleteBtn = view.findViewById(R.id.ivSettingsServiceDeleteBtn)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.worker_service_for_settings, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvSettingsServiceName.text = data[position].name

        holder.ivSettingsServiceDeleteBtn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val querySnapshot = workerCatCollectionRef
                        .whereEqualTo("cName", data[position].name)
                        .whereEqualTo("wEmail", email)
                        .get()
                        .await()

                    for (document in querySnapshot.documents) {
                        workerCatCollectionRef.document(document.id).delete()
                    }

                    val categories = mutableListOf<Category>()

                    val querySnapshot2 = workerCatCollectionRef
                        .whereEqualTo("wEmail", email)
                        .get()
                        .await()

                    for (document in querySnapshot2.documents) {
                        val name = document.get("cName").toString()
                        val rate = document.get("hrRate").toString()
                        val description = document.get("wDesc").toString()

                        val category = Category(name, description, "", rate)

                        categories.add(category)
                    }

                    withContext(Dispatchers.Main) {
                        setData(categories, context)
                    }

                } catch (e: Exception) {
                    println(e.message)
                }
            }

        }


        holder.ivSettingsServiceEditBtn.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val inflator: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view: View = inflator.inflate(R.layout.edit_service_dialog, null)

            // Set the view
            builder.setView(view)

            val tvEditServiceName = view.findViewById<TextView>(R.id.tvEditServiceName)
            val etEditServiceDesc = view.findViewById<EditText>(R.id.etEditServiceDesc)
            val etEditServiceHrRate = view.findViewById<EditText>(R.id.etEditServiceHrRate)

            tvEditServiceName.text = data[position].name
            etEditServiceDesc.setText(data[position].description)
            etEditServiceHrRate.setText(data[position].price)

            // Set the positive button action
            builder.setPositiveButton("EDIT") { dialog, which ->
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val querySnapshot = workerCatCollectionRef
                            .whereEqualTo("cName", data[position].name)
                            .whereEqualTo("wEmail", email)
                            .get()
                            .await()

                        for (document in querySnapshot.documents) {
                            val worker = document.toObject<Worker>()

                            if (etEditServiceDesc.text.toString().isNotEmpty()) workerCatCollectionRef.document(document.id)
                                .update("wDesc", etEditServiceDesc.text.toString())
                            if (etEditServiceHrRate.text.toString().isNotEmpty()) workerCatCollectionRef.document(document.id)
                                .update("hrRate", etEditServiceHrRate.text.toString())
                        }

                        val categories = mutableListOf<Category>()

                        val querySnapshot2 = workerCatCollectionRef
                            .whereEqualTo("wEmail", email)
                            .get()
                            .await()

                        for (document in querySnapshot2.documents) {
                            val name = document.get("cName").toString()
                            val rate = document.get("hrRate").toString()
                            val description = document.get("wDesc").toString()

                            val category = Category(name, description, "", rate)

                            categories.add(category)
                        }

                        withContext(Dispatchers.Main) {
                            setData(categories, context)
                        }

                    } catch (e: Exception) {
                        println(e.message)
                    }
                }

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

    fun setData(data: List<Category>, context: Context) {
        this.data = data
        this.context = context
        notifyDataSetChanged()
    }

}