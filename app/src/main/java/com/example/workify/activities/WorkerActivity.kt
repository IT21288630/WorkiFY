package com.example.workify.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.workify.R
import com.example.workify.fragments.WorkerMessagesFragment
import com.example.workify.fragments.WorkerOrdersFragment
import com.example.workify.fragments.MainWorkerProfileFragment
import com.example.workify.fragments.WorkerSettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class WorkerActivity : AppCompatActivity() {

    private val workerCollectionRef = Firebase.firestore.collection("workers")
    private var currentEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker)

        val workerBottomNavigationView =
            findViewById<BottomNavigationView>(R.id.workerBottomNavigationView)
        val mainWorkerProfileFragment = MainWorkerProfileFragment()
        val workerSettingsFragment = WorkerSettingsFragment()
        val workerMessagesFragment = WorkerMessagesFragment()

        val workerOrdersFragment = WorkerOrdersFragment()

        val mBundle = Bundle()
        mBundle.putString("curWorkerEmail", intent.getStringExtra("curWorkerEmail"))

        currentEmail = intent.getStringExtra("curWorkerEmail")

        getToken()

        setCurrentFragment(mainWorkerProfileFragment, mBundle)

        workerBottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.miProfile -> setCurrentFragment(mainWorkerProfileFragment, mBundle)
                R.id.miSettings -> setCurrentFragment(workerSettingsFragment, mBundle)
                R.id.miNotification -> setCurrentFragment(workerOrdersFragment, mBundle)
                R.id.miMessage -> setCurrentFragment(workerMessagesFragment, mBundle)
            }
            true
        }

    }

    private fun setCurrentFragment(fragment: Fragment, mBundle: Bundle) {
        fragment.arguments = mBundle
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flWorkerFragment, fragment)
            commit()
        }
    }

    private fun getToken() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener(this::updateToken)
    }

    private fun updateToken(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot =
                    workerCollectionRef.whereEqualTo("email", currentEmail).get().await()

                for (document in querySnapshot.documents) {
                    workerCollectionRef.document(document.id)
                        .update("fcmToken", token)
                }
            } catch (e: Exception) {
                println(e.message)
            }

        }
    }

}