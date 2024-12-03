package com.example.wellcheck.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

import com.bumptech.glide.Glide
import com.example.wellcheck.Domain.Doctors

import com.example.wellcheck.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var item: Doctors
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getBundle()

    }

    private fun getBundle() {
        item=intent.getParcelableExtra("object")!!

        binding.apply {

            titleTxt.text=item.name
            specialTxt.text=item.special
            patientsTxt.text=item.patiens
            bio.text=item.biography
            addressTxt.text=item.address
            experienceTxt.text=item.expriense.toString()+" Years"
            ratingTxt.text="${item.rating}"
            backBtn.setOnClickListener{finish()}
            webisteBtn.setOnClickListener {
                val i =Intent(Intent.ACTION_VIEW)
                i.setData(Uri.parse(item.site))
                startActivity(i)
            }

            messageBtn.setOnClickListener {
                val uri=Uri.parse("smsto:${item.mobile}")
                val intent=Intent(Intent.ACTION_SENDTO,uri)
                intent.putExtra("sms_body","the SMS text")
                startActivity(intent)

            }
            CallBtn.setOnClickListener {
                val uri ="tel:"+item.mobile.trim()
                val intent=Intent(Intent.ACTION_DIAL,Uri.parse(uri))
                startActivity(intent)
            }
            directionBtn.setOnClickListener {
                val intent=Intent(Intent.ACTION_VIEW,Uri.parse(item.location))
                startActivity(intent)
            }
            shareBtn.setOnClickListener {
                val intent=Intent(Intent.ACTION_SEND)
                intent.setType("text/plain")
                intent.putExtra(Intent.EXTRA_SUBJECT,item.name)
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    item.name + " " + item.address + " " + item.mobile
                )
                startActivity(Intent.createChooser(intent,"Choose one"))
            }
            makeBtn.setOnClickListener {
                // Navigate to MakeAppointmentActivity
                val intent = Intent(this@DetailActivity, MakeAppointmentActivity::class.java)
                intent.putExtra("doctorId", item.id)
                intent.putExtra("doctorName", item.name)// Pass doctor ID or any relevant info
                startActivity(intent)
            }


Glide.with(this@DetailActivity)
    .load(item.picture)
    .into(img)


        }
    }
}