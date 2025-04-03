package com.example.lab7

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
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

        edUsername = findViewById(R.id.ed_username)
        edPassword = findViewById(R.id.ed_password)
        edConfirmPassword = findViewById(R.id.ed_confirm_pwd)
        btnCreateUser = findViewById(R.id.btn_create_user)

        edUsername.filters = arrayOf(InputFilter.LengthFilter(12))

        btnCreateUser.setOnClickListener {
            val strUsername = edUsername.text.toString()
            val strPassword = edPassword.text.toString()
            val strConfirmPassword = edConfirmPassword.text.toString()

            if (strUsername.contains(' ')) {
                Toast.makeText(this, "Need to remove space in username", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (strPassword.contains(' ')) {
                Toast.makeText(this, "You need to remove the space in the password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (strPassword == strConfirmPassword) {
                val preferences: SharedPreferences = getSharedPreferences(CREDENTIAL_SHARED_PREF, Context.MODE_PRIVATE)
                val editor = preferences.edit()
                editor.putString("Username", strUsername)
                editor.putString("Password", strPassword)
                editor.apply()

                Toast.makeText(this, "User created!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "The passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }
    }
}