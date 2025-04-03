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

    // Имя файла (во внутреннем хранилище)
    private val FILE_NAME = "MyStorageApp.txt"

    // Для SharedPreferences (хранение логина и пароля)
    private val CREDENTIAL_SHARED_PREF = "our_shared_pref"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvUsername = findViewById(R.id.tvUsername)
        edData = findViewById(R.id.edData)
        btnWrite = findViewById(R.id.btnWriteFile)
        btnRead = findViewById(R.id.btnReadFile)
        btnLogout = findViewById(R.id.btnLogout)

        // Считываем имя пользователя из SharedPreferences и отображаем
        val preferences: SharedPreferences =
            getSharedPreferences(CREDENTIAL_SHARED_PREF, Context.MODE_PRIVATE)
        val username = preferences.getString("Username", "Unknown")
        tvUsername.text = "Hello, $username!"

        // Кнопка «Записать в файл»
        btnWrite.setOnClickListener {
            val textToSave = edData.text.toString()
            writeToFile(textToSave)
        }

        // Кнопка «Прочитать из файла»
        btnRead.setOnClickListener {
            val fileData = readFromFile()
            edData.setText(fileData) // отображаем данные в EditText
        }

        // Кнопка «Выйти» (очистка данных и возврат на LoginActivity)
        btnLogout.setOnClickListener {
            val editor = preferences.edit()
            editor.clear()
            editor.apply()

            val intent = Intent(this, LoginActivity::class.java)
            // Чтобы пользователь не мог вернуться кнопкой «Назад»
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    /**
     * Запись во внутреннее хранилище (internal storage).
     * Файл будет создан или перезаписан внутри каталога приложения.
     */
    private fun writeToFile(text: String) {
        try {
            // openFileOutput откроет FileOutputStream во внутреннем каталоге приложения
            val outputStream = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
            val writer = OutputStreamWriter(outputStream)
            writer.write(text)
            writer.close()
            Toast.makeText(this, "Data saved to $FILE_NAME", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error saving file: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Чтение из внутреннего файла.
     */
    private fun readFromFile(): String {
        val stringBuilder = StringBuilder()
        try {
            val inputStream = openFileInput(FILE_NAME)
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String? = reader.readLine()
            while (line != null) {
                stringBuilder.append(line).append("\n")
                line = reader.readLine()
            }
            reader.close()
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error reading file: ${e.message}", Toast.LENGTH_LONG).show()
        }
        return stringBuilder.toString()
    }

}