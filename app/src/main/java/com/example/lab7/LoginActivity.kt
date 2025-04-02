package com.example.lab7

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {

    private lateinit var edUsername: EditText
    private lateinit var edPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: Button

    private val CREDENTIAL_SHARED_PREF = "our_shared_pref"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Инициализируем вьюхи
        edUsername = findViewById(R.id.ed_username)
        edPassword = findViewById(R.id.ed_password)
        btnLogin   = findViewById(R.id.btn_login)
        btnSignUp  = findViewById(R.id.btn_signup)

        // Кнопка регистрации
        btnSignUp.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

        // Кнопка входа
        btnLogin.setOnClickListener {
            val preferences: SharedPreferences = getSharedPreferences(CREDENTIAL_SHARED_PREF, Context.MODE_PRIVATE)

            val savedUsername = preferences.getString("Username", null)
            val savedPassword = preferences.getString("Password", null)

            val inputUsername = edUsername.text.toString()
            val inputPassword = edPassword.text.toString()

            if (savedUsername != null && savedUsername.equals(inputUsername, ignoreCase = true)) {
                if (savedPassword != null && savedPassword.equals(inputPassword, ignoreCase = true)) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}