package com.example.wellcheck

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.wellcheck.Domain.Doctors
import com.example.wellcheck.databinding.ActivitySeeProfileBinding

class seeProfile : AppCompatActivity() {
    private lateinit var binding: ActivitySeeProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeeProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve doctor object from intent
        val doctor: Doctors? = intent.getParcelableExtra("doctor")

        if (doctor != null) {
            displayDoctorInfo(doctor)
        }
    }

    private fun displayDoctorInfo(doctor: Doctors) {
        binding.apply {
            titleTxt.text = doctor.name
            specialTxt.text = doctor.special
            patientsTxt.text = doctor.patiens
            bio.text = doctor.biography
            addressTxt.text = doctor.address
            experienceTxt.text = "${doctor.expriense} Years"
            ratingTxt.text = "${doctor.rating}"

            Glide.with(this@seeProfile)
                .load(doctor.picture)
                .into(img)

            backBtn.setOnClickListener { finish() }

            webisteBtn.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(doctor.site))
                startActivity(intent)
            }

            messageBtn.setOnClickListener {
                val uri = Uri.parse("smsto:${doctor.mobile}")
                val intent = Intent(Intent.ACTION_SENDTO, uri)
                intent.putExtra("sms_body", "Hello")
                startActivity(intent)
            }

            CallBtn.setOnClickListener {
                val uri = "tel:${doctor.mobile.trim()}"
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse(uri))
                startActivity(intent)
            }

            directionBtn.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(doctor.location))
                startActivity(intent)
            }

            shareBtn.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, doctor.name)
                    putExtra(Intent.EXTRA_TEXT, "${doctor.name} ${doctor.address} ${doctor.mobile}")
                }
                startActivity(Intent.createChooser(intent, "Choose one"))
            }
        }
    }
}
