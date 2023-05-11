package com.example.workify.fragments

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workify.R
import com.example.workify.activities.HomeSearchActivity
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
        val etHomeHelpSearch = view.findViewById<EditText>(R.id.etHomeHelpSearch)

        val bundle = arguments
        val email = bundle!!.getString("curCusEmail")

        etHomeHelpSearch.setOnKeyListener { view, keyCode, keyevent ->
            //If the keyevent is a key-down event on the "enter" button
            if (keyevent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                var intent = Intent(view.context, HomeSearchActivity::class.java)
                intent.putExtra("serviceNameFromHome", etHomeHelpSearch.text.toString())
                intent.putExtra("curCusEmail", email)
                startActivity(intent)
                true
            } else false
        }


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
                    val adapter = email?.let { ServicesForHomeAdapter(services, view.context, it) }
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = GridLayoutManager(view.context, 3)
                    adapter?.setData(services, view.context)
                }


            } catch (e: Exception) {
                println(e.message)
            }
        }
    }
}