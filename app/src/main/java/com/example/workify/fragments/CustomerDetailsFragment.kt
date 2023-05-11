package com.example.workify.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.workify.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.example.workify.dataClasses.Customer

class CustomerDetailsFragment : Fragment(R.layout.fragment_cutomer_details) {

   /* private lateinit var etWSName: TextView
    private lateinit var etWSEmail: TextView
    private lateinit var etWSDistrict: TextView
    private val db = Firebase.firestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cutomer_details, container, false)

        etWSName = view.findViewById(R.id.etWSName)
        etWSEmail = view.findViewById(R.id.etWSEmail)
        etWSDistrict = view.findViewById(R.id.etWSDistrict)

        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = db.collection("Customers").document(userId)

        ref.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val customer = documentSnapshot.toObject<Customer>()
                etWSName.text = customer?.name
                etWSEmail.text = customer?.email
                etWSDistrict.text = customer?.district
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(requireContext(), "Failed to load customer details: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
        return view
    }*/
}