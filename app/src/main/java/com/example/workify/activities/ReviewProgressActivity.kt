package com.example.workify.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.workify.R

class ReviewProgressActivity : AppCompatActivity(){

    private val TIMEOUT: Long = 5000
    private val handler: Handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.progress_review)

        handler.postDelayed(runnable, TIMEOUT)

        val intent = Intent(this, CustomerReviewAddActivity::class.java)
        startActivity(intent)
    }
}