package com.example.lab7

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class MainActivity : AppCompatActivity() {

    private lateinit var btnWriteFile: Button
    private lateinit var btnReadFile: Button
    private lateinit var tvFileData: TextView
    private lateinit var tvWelcome: TextView

    companion object {
        private const val REQUEST_CODE_WRITE_PERM = 401
        private const val FILE_NAME = "userdata.txt"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnWriteFile = findViewById(R.id.btnWriteFile)
        btnReadFile  = findViewById(R.id.btnReadFile)
        tvFileData   = findViewById(R.id.tvFileData)
        tvWelcome    = findViewById(R.id.tvWelcome)

        // Читаем имя пользователя из SharedPreferences
        val credentials = getSharedPreferences("user_credentials", Context.MODE_PRIVATE)
        val username = credentials.getString("Username", "User")
        tvWelcome.text = "Welcome, $username!"

        // Проверяем/запрашиваем разрешение на запись во внешнее хранилище
        requestNeededPermission()

        // Кнопка «Записать в файл»
        btnWriteFile.setOnClickListener {
            writeFile()  // username уже не вписываем в саму строку файла
            Toast.makeText(this, "File written successfully", Toast.LENGTH_SHORT).show()
        }

        // Кнопка «Прочитать из файла»
        btnReadFile.setOnClickListener {
            val data = readFile()
            tvFileData.text = if (data.isNotEmpty()) data else "No data found"
        }
    }

    /**
     * Запрашиваем разрешение на запись во внешнее хранилище (WRITE_EXTERNAL_STORAGE).
     */
    private fun requestNeededPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE_WRITE_PERM
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_WRITE_PERM -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Storage permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * Запись во внешний файл. Только дата/время (UTC).
     * Убрали строку «Current User's Login».
     */
    private fun writeFile() {
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            val currentDateTime = dateFormat.format(Date())

            val content = """
                Hello(UTC - YYYY-MM-DD HH:MM:SS formatted): $currentDateTime
            """.trimIndent()  // ← Текст, который запишется в файл

            val file = File(getExternalFilesDir(null), FILE_NAME)

            FileOutputStream(file).use { fos ->
                fos.write(content.toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error writing file: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Чтение содержимого userdata.txt (во внешнем каталоге приложения).
     */
    private fun readFile(): String {
        var result = ""
        try {
            val file = File(getExternalFilesDir(null), FILE_NAME)
            if (!file.exists()) {
                return "No file exists yet. Click Write to File first."
            }

            FileInputStream(file).use { fis ->
                val bos = ByteArrayOutputStream()
                var b = fis.read()
                while (b != -1) {
                    bos.write(b)
                    b = fis.read()
                }
                result = bos.toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            result = "Error reading file: ${e.message}"
        }
        return result
    }
}