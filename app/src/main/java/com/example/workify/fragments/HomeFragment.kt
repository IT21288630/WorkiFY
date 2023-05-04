package com.example.workify.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.R
import com.example.workify.adapters.ServicesForHomeAdapter
import com.example.workify.dataClasses.Category
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val categoryCollectionRef = Firebase.firestore.collection("categories")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvHomeServices)

        var services = mutableListOf<Category>()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = categoryCollectionRef
                    .get()
                    .await()

                for (document in querySnapshot.documents) {
                    val name = document.get("name").toString()
                    val image = document.get("image").toString()

                    val category = Category(name, "", image, "")

                    services.add(category)
                }

                withContext(Dispatchers.Main) {
                    val adapter = ServicesForHomeAdapter(services, view.context)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = GridLayoutManager(view.context, 3)
                    adapter.setData(services, view.context)
                }


            } catch (e: Exception) {
                println(e.message)
            }
        }
    }
}