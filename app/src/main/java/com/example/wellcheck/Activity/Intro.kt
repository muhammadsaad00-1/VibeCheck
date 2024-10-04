package com.example.wellcheck.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.wellcheck.R

class Intro : AppCompatActivity() {
    private lateinit var btnLetsGo: Button  // Use Button instead of EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_intro)

        // Correctly reference the button
        btnLetsGo = findViewById(R.id.button)

        // Set OnClickListener to navigate to the next screen (Abc.kt) when the button is pressed
        btnLetsGo.setOnClickListener {
            val intent = Intent(this, Abc::class.java)  // Create an intent to navigate to Abc
            startActivity(intent)  // Start the activity
        }
    }
}