package com.example.lab7

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var tvWelcome: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvWelcome = findViewById(R.id.tvWelcome)

        val currentUser = intent.getStringExtra("EXTRA_LOGIN") ?: "UnknownUser"
        tvWelcome.text = "Welcome, $currentUser!"

        val prefName = "$currentUser-OutSharedPref"
        val userPref = getSharedPreferences(prefName, Context.MODE_PRIVATE)
    }
}