package com.example.workify.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.*
import com.example.workify.dataClasses.Category
import com.example.workify.fragments.WorkerSettingsFragment
import com.google.firebase.firestore.ktx.firestore
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
            var workerSettingsFragment = WorkerSettingsFragment()

            //workerSettingsFragment.displayEditServiceDialog(data[position].name, data[position].description, data[position].price)
        }
    }

    fun setData(data: List<Category>, context: Context) {
        this.data = data
        this.context = context
        notifyDataSetChanged()
    }

}