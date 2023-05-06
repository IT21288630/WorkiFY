package com.example.workify

import android.content.Intent
import android.widget.Toast
import com.example.workify.activities.MainActivity
import com.example.workify.activities.WorkerActivity
import com.example.workify.dataClasses.Worker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mockito

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun login_isCorrect() {
        val workerCollectionRef = Firebase.firestore.collection("workers")

        var result: Boolean? = null

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = workerCollectionRef
                    .whereEqualTo("email", "qwe")
                    .whereEqualTo("password", "454")
                    .get()
                    .await()

                if (querySnapshot.isEmpty) {
                    withContext(Dispatchers.Main) {
                        result = false
                        return@withContext
                    }
                }

                for (document in querySnapshot.documents) {
                    result = true
                }

            } catch (e: Exception) {
                result = false
                return@launch
            }
        }

        assertEquals(true, result)
    }

    @Test
    fun test_isCorrect() {
        var result: Boolean? = null
    }

}