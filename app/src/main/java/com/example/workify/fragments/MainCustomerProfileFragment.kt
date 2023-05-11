package com.example.workify.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.workify.R
import com.example.workify.dataClasses.Customer
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
class MainCustomerProfileFragment : Fragment(R.layout.fragment_main_customer_profile) {

    private val workerCollectionRef = Firebase.firestore.collection("customers")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val customerProfileFavFragment = CustomerProfileFavFragment()
        val customerDetailsFragment = CustomerDetailsFragment()
        val customerOrdersFragment = CustomerOrdersFragment()

        val bundle = arguments
        val email = bundle!!.getString("curCusEmail")

        val mBundle = Bundle()
        mBundle.putString("curCusEmail", email)

        customerDetailsFragment.arguments = mBundle
        childFragmentManager.beginTransaction().apply {
            replace(R.id.flSubCusProfileFragment, customerDetailsFragment)
            commit()
        }

        val cusDetailsBtn = view.findViewById<Button>(R.id.cusDetailsBtn)
        val cusFavBtn = view.findViewById<Button>(R.id.cusFavBtn)
        val cusRevBtn = view.findViewById<Button>(R.id.cusRevBtn)
        val cusOrdersBtn = view.findViewById<Button>(R.id.cusOrdersBtn)

       cusDetailsBtn.setOnClickListener {
           customerDetailsFragment.arguments = mBundle
            childFragmentManager.beginTransaction().apply {
                replace(R.id.flSubCusProfileFragment, customerDetailsFragment)
                addToBackStack(null)
                commit()
            }
        }

       cusFavBtn.setOnClickListener {
           customerProfileFavFragment.arguments = mBundle
            childFragmentManager.beginTransaction().apply {
                replace(R.id.flSubCusProfileFragment, customerProfileFavFragment)
                addToBackStack(null)
                commit()
            }
        }

        cusOrdersBtn.setOnClickListener {
            customerOrdersFragment.arguments = mBundle
            childFragmentManager.beginTransaction().apply {
                replace(R.id.flSubCusProfileFragment, customerOrdersFragment)
                addToBackStack(null)
                commit()
            }
        }
        val tvWorkerName = view.findViewById<TextView>(R.id.tvWorkerNameSett)
        val tvWorkerEmail = view.findViewById<TextView>(R.id.tvWorkerEmailSett)


        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = workerCollectionRef
                    .whereEqualTo("email", email)
                    .get()
                    .await()

                for (document in querySnapshot.documents) {
                    val worker = document.toObject<Customer>()

                    if (worker != null) {
                        withContext(Dispatchers.Main){
                            tvWorkerName.text = worker.name
                            tvWorkerEmail.text = worker.email
                        }
                    }
                }

            } catch (e: Exception) {
                println(e.message)
            }
        }
    }


}