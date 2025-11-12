  package com.example.services

  import android.content.Intent
  import android.os.Bundle
  import android.widget.Button
  import android.widget.EditText
  import android.widget.Toast
  import androidx.appcompat.app.AppCompatActivity

  class MainActivity : AppCompatActivity() {

      override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
          setContentView(R.layout.activity_main)

          val input = findViewById<EditText>(R.id.secondsInput)
          val startBtn = findViewById<Button>(R.id.startButton)

          startBtn.setOnClickListener {
              val text = input.text?.toString()?.trim().orEmpty()
              val seconds = text.toIntOrNull()

              if (seconds == null || seconds <= 0) {
                  Toast.makeText(this, "Please enter a positive integer.", Toast.LENGTH_SHORT).show()
                  return@setOnClickListener
              }

              val intent = Intent(this, CountdownService::class.java).apply {
                  putExtra(CountdownService.EXTRA_SECONDS, seconds)
              }
              // Start a *Started* Service (not bound)
              startService(intent)
          }
      }
  }
