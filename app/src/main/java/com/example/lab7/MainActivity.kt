package com.example.lab7

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class MainActivity : AppCompatActivity() {

    private lateinit var tvUsername: TextView
    private lateinit var edData: EditText
    private lateinit var btnWrite: Button
    private lateinit var btnRead: Button
    private lateinit var btnLogout: Button

    // Название файла внутри приложения
    private val FILE_NAME = "MyStorageApp.txt"

    // Для чтения логина (SharedPreferences)
    private val CREDENTIAL_SHARED_PREF = "our_shared_pref"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvUsername = findViewById(R.id.tvUsername)
        edData     = findViewById(R.id.edData)
        btnWrite   = findViewById(R.id.btnWrite)
        btnRead    = findViewById(R.id.btnRead)
        btnLogout  = findViewById(R.id.btnLogout)

        // Считываем пользователя из SharedPreferences
        val prefs: SharedPreferences = getSharedPreferences(CREDENTIAL_SHARED_PREF, Context.MODE_PRIVATE)
        val username = prefs.getString("Username", "Unknown")
        tvUsername.text = "Hello, $username!"

        // Кнопка: «Записать в файл»
        btnWrite.setOnClickListener {
            val textToSave = edData.text.toString()
            writeToFile(textToSave)
        }

        // Кнопка: «Прочитать из файла»
        btnRead.setOnClickListener {
            val fileData = readFromFile()
            edData.setText(fileData)
        }

        // Кнопка: «Выйти» — очистка SharedPreferences и возвращение на LoginActivity
        btnLogout.setOnClickListener {
            prefs.edit().clear().apply()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun writeToFile(text: String) {
        try {
            // Записываем во внутреннее хранилище (Context.MODE_PRIVATE)
            val outputStream = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
            val writer = OutputStreamWriter(outputStream)
            writer.write(text)
            writer.close()
            Toast.makeText(this, "Data saved to $FILE_NAME", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error writing file: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun readFromFile(): String {
        val sb = StringBuilder()
        try {
            val inputStream = openFileInput(FILE_NAME)
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String? = reader.readLine()
            while (line != null) {
                sb.append(line).append("\n")
                line = reader.readLine()
            }
            reader.close()
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error reading file: ${e.message}", Toast.LENGTH_LONG).show()
        }
        return sb.toString()
    }
}