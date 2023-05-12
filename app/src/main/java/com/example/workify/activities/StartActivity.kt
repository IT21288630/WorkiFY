package com.example.workify.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.workify.R

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val hireBtn = findViewById<Button>(R.id.hireBtn)
        val workBtn = findViewById<Button>(R.id.workBtn)

        hireBtn.setOnClickListener {
            val intent = Intent(this@StartActivity, CustomerLoginActivity2::class.java)
            startActivity(intent)
            finish()
        }

        workBtn.setOnClickListener {
            val intent = Intent(this@StartActivity, WorkerLoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}