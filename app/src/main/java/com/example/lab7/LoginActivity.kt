package com.example.lab7

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {

    private lateinit var edUsername: EditText
    private lateinit var edPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edUsername = findViewById(R.id.ed_username)
        edPassword = findViewById(R.id.ed_password)
        btnLogin   = findViewById(R.id.btn_login)
        btnSignUp  = findViewById(R.id.btn_signup)

        btnSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        btnLogin.setOnClickListener {
            val usernameFromEd = edUsername.text.toString().trim()
            val passwordFromEd = edPassword.text.toString()

            if (usernameFromEd.isEmpty()) {
                Toast.makeText(this, "Username is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val prefName = "$usernameFromEd-OutSharedPref"
            val userPref = getSharedPreferences(prefName, Context.MODE_PRIVATE)
            val storedUser = userPref.getString("Username", null)
            val storedPass = userPref.getString("Password", null)

            if (storedUser == null) {
                Toast.makeText(this, "User '$usernameFromEd' not found", Toast.LENGTH_SHORT).show()
            } else if (storedPass == passwordFromEd) {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("EXTRA_LOGIN", usernameFromEd)
                startActivity(intent)
                finish()

            } else {
                Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}