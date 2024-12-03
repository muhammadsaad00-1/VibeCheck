package com.example.wellcheck.Activity


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.wellcheck.R

import com.example.wellcheck.TestPage
import com.example.wellcheck.UploadFiles

class NaviActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navi)

        // Initialize buttons
        val btnHome = findViewById<Button>(R.id.btn_home)
        val btnBmiCalculator = findViewById<Button>(R.id.btn_bmi_calculator)
        val btnProfile = findViewById<Button>(R.id.btn_profile)
        val btnTip = findViewById<Button>(R.id.btn_tip)
        val btnDonate = findViewById<Button>(R.id.btn_donate)
        val btnAboutUs = findViewById<Button>(R.id.btn_aboutus)
        val btnReportIssue = findViewById<Button>(R.id.btn_reportissue) // Add Report Issue Button
        val btnUploadReport = findViewById<Button>(R.id.btn_upload_report) // Add Upload Report Button

        // Set click listeners for each button
        btnHome.setOnClickListener {
            val intent = Intent(this, Abc::class.java)
            startActivity(intent)
        }
btnUploadReport.setOnClickListener {
    val intent = Intent(this, UploadFiles::class.java)
    startActivity(intent)
}
        btnBmiCalculator.setOnClickListener {
            val intent = Intent(this, Bmi::class.java)
            startActivity(intent)
        }
        btnReportIssue.setOnClickListener {
            val intent = Intent(this, Issue::class.java)
            startActivity(intent)
        }

        btnProfile.setOnClickListener {
            val intent = Intent(this, TestPage::class.java)
        startActivity(intent)
        }

        btnTip.setOnClickListener {

            val intent = Intent(this, ViewRemindersActivity::class.java)

            startActivity(intent)
        }

        btnDonate.setOnClickListener {
            val intent = Intent(this, Donate::class.java)
            startActivity(intent)
        }

        btnAboutUs.setOnClickListener {
            val intent = Intent(this, Aboutus::class.java)
            startActivity(intent)
        }

    }
}