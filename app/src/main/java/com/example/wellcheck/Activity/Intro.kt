package com.example.wellcheck.Activity
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.wellcheck.R
import com.example.wellcheck.Login
import com.example.wellcheck.PartialLogin

class Intro : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val loginDoctorButton: Button = findViewById(R.id.btn_login_doctor)
        val loginPatientButton: Button = findViewById(R.id.btn_login_patient)


        loginDoctorButton.setOnClickListener {

            val intent = Intent(this, PartialLogin::class.java)
            startActivity(intent)
        }

        loginPatientButton.setOnClickListener {

            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }
}
