package com.example.wellcheck.Activity
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.wellcheck.Domain.Doctors
import com.example.wellcheck.R



class DocNav : AppCompatActivity() {

    private lateinit var doctor: Doctors
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doc_nav)

        val btnHome = findViewById<Button>(R.id.btn_home)
        val btnBmiCalculator = findViewById<Button>(R.id.btn_bmi_calculator)

        val btnDonate = findViewById<Button>(R.id.btn_donate)
        val btnAboutUs = findViewById<Button>(R.id.btn_aboutus)
        val btnReportIssue = findViewById<Button>(R.id.btn_reportissue)

        val btnSwitchAccount = findViewById<Button>(R.id.btn_switchacc)

        val crossButton = findViewById<ImageView>(R.id.img_close)
        doctor = intent.getParcelableExtra("doctor") ?: return
        intent.putExtra("doctor", doctor)

        btnHome.setOnClickListener {
            val intent = Intent(this, EditDoctorProfile::class.java)
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


        val btnTip = findViewById<Button>(R.id.btn_tip)
        btnTip.setOnClickListener {
             val intent = Intent(this, PostTipActivity::class.java)
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

        btnSwitchAccount.setOnClickListener {
            val intent = Intent(this, Intro::class.java)
            startActivity(intent)
        }


        crossButton.setOnClickListener {
            finish()
        }
    }
}
