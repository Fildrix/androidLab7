package com.example.lab7

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class SignUpActivity : AppCompatActivity() {
    private lateinit var edUsername: EditText
    private lateinit var edPassword: EditText
    private lateinit var edConfirmPassword: EditText
    private lateinit var btnCreateUser: Button

    private val CREDENTIAL_SHARED_PREF = "our_shared_pref"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Инициализация
        edUsername         = findViewById(R.id.ed_username)
        edPassword         = findViewById(R.id.ed_password)
        edConfirmPassword  = findViewById(R.id.ed_confirm_pwd)
        btnCreateUser      = findViewById(R.id.btn_create_user)

        btnCreateUser.setOnClickListener {
            val strPassword = edPassword.text.toString()
            val strConfirm = edConfirmPassword.text.toString()
            val strUsername = edUsername.text.toString()

            // Проверяем совпадение паролей
            if (strPassword.equals(strConfirm, ignoreCase = true)) {
                val preferences: SharedPreferences = getSharedPreferences(CREDENTIAL_SHARED_PREF, Context.MODE_PRIVATE)
                val editor = preferences.edit()
                editor.putString("Password", strPassword)
                editor.putString("Username", strUsername)
                editor.apply()

                Toast.makeText(this, "User created!", Toast.LENGTH_SHORT).show()
                finish() // Закрываем экран регистрации
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }
    }
}