package com.example.lab7

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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

    private val CREDENTIAL_SHARED_PREF = "our_shared_pref"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edUsername = findViewById(R.id.ed_username)
        edPassword = findViewById(R.id.ed_password)
        btnLogin   = findViewById(R.id.btn_login)
        btnSignUp  = findViewById(R.id.btn_signup)

        // Переход на экран регистрации
        btnSignUp.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

        // Кнопка «Войти»
        btnLogin.setOnClickListener {
            val credentials: SharedPreferences = getSharedPreferences(CREDENTIAL_SHARED_PREF, Context.MODE_PRIVATE)
            val strUsername = credentials.getString("Username", null)
            val strPassword = credentials.getString("Password", null)

            val usernameFromEd = edUsername.text.toString()
            val passwordFromEd = edPassword.text.toString()

            if (strUsername != null && strUsername.equals(usernameFromEd, ignoreCase = true)) {
                if (strPassword != null && strPassword.equals(passwordFromEd, ignoreCase = true)) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()

                    // Переход в MainActivity, где есть кнопки Read/Write файла
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Чтобы при нажатии «Назад» не возвращаться сюда
                } else {
                    Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}