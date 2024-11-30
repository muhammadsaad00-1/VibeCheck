package com.example.wellcheck

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.wellcheck.Activity.DoctorAppointmentsActivity
import com.example.wellcheck.Activity.EditDoctorProfile
import com.example.wellcheck.Domain.Doctors
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class west : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var tvName: TextView
    private lateinit var tvBiography: TextView
    private lateinit var tvAddress: TextView
    private lateinit var tvSpecial: TextView
    private lateinit var tvLocation: TextView
    private lateinit var tvMobile: TextView
    private lateinit var tvPatients: TextView
    private lateinit var tvSite: TextView
    private lateinit var tvRating: TextView
    private lateinit var tvExperience: TextView
    private lateinit var btnViewProfile: Button
    private lateinit var btnEditProfile: Button
    private lateinit var btnAppointment: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_west)

        auth = FirebaseAuth.getInstance()

        // Initialize UI components
        tvName = findViewById(R.id.tvName)
        tvBiography = findViewById(R.id.tvBiography)
        tvAddress = findViewById(R.id.tvAddress)
        tvSpecial = findViewById(R.id.tvSpecial)
        tvLocation = findViewById(R.id.tvLocation)
        tvMobile = findViewById(R.id.tvMobile)
        tvPatients = findViewById(R.id.tvPatients)
        tvSite = findViewById(R.id.tvSite)
        tvRating = findViewById(R.id.tvRating)
        tvExperience = findViewById(R.id.tvExperience)
        btnViewProfile = findViewById(R.id.btnn)
        btnEditProfile = findViewById(R.id.btnnn)
        btnAppointment = findViewById(R.id.bt)
        // Load doctor info from Firebase
        loadDoctorInfo()

        // Set up edit profile button
        btnEditProfile.setOnClickListener {
            getDoctor { doctor ->
                val intent = Intent(this, EditDoctorProfile::class.java)
                intent.putExtra("doctor", doctor)  // Pass doctor data to EditDoctorProfile
                startActivity(intent)
            }
        }
        btnAppointment.setOnClickListener{
            val intent = Intent(this, DoctorAppointmentsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadDoctorInfo() {
        val uid = auth.currentUser?.uid ?: return
        val ref = FirebaseDatabase.getInstance().reference.child("doctors").child(uid)

        // Use a ValueEventListener to listen for changes in the data
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Extract doctor data
                    val doctor =
                        Doctors(
                            id=snapshot.child("id").value?.toString() ?: "",
                        name = snapshot.child("name").value?.toString() ?: "",
                        picture = snapshot.child("picture").value?.toString() ?: "",
                        biography = snapshot.child("biography").value?.toString() ?: "",
                        address = snapshot.child("address").value?.toString() ?: "",
                        special = snapshot.child("special").value?.toString() ?: "",
                        location = snapshot.child("location").value?.toString() ?: "",
                        mobile = snapshot.child("mobile").value?.toString() ?: "",
                        patiens = snapshot.child("patiens").value?.toString() ?: "",
                        site = snapshot.child("site").value?.toString() ?: "",
                        rating = snapshot.child("rating").value?.toString()?.toDoubleOrNull() ?: 0.0,
                        expriense = snapshot.child("expriense").value?.toString()?.toDoubleOrNull() ?: 0.0
                    )

                    // Set updated data to the current activity's views
                    tvName.text = "Name: ${doctor.name}"
                    tvBiography.text = "Biography: ${doctor.biography}"
                    tvAddress.text = "Address: ${doctor.address}"
                    tvSpecial.text = "Specialization: ${doctor.special}"
                    tvLocation.text = "Location: ${doctor.location}"
                    tvMobile.text = "Mobile: ${doctor.mobile}"
                    tvPatients.text = "Patients: ${doctor.patiens}"
                    tvSite.text = "Website: ${doctor.site}"
                    tvRating.text = "Rating: ${doctor.rating}"
                    tvExperience.text = "Experience: ${doctor.expriense} years"

                    // Optionally, load the profile image using Glide
                   // Glide.with(this@west).load(doctor.Picture).into(findViewById<ImageView>(R.id.ivProfilePicture))

                    // View profile button click
                    btnViewProfile.setOnClickListener {
                        val intent = Intent(this@west, seeProfile::class.java)
                        intent.putExtra("doctor", doctor)  // Pass doctor object
                        startActivity(intent)
                    }
                } else {
                    // Handle case when data doesn't exist
                    tvName.text = "Failed to load data."
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle potential errors
                tvName.text = "Failed to load data."
            }
        })
    }

    private fun getDoctor(onDoctorLoaded: (Doctors?) -> Unit) {
        val uid = auth.currentUser?.uid ?: return
        val ref = FirebaseDatabase.getInstance().reference.child("doctors").child(uid)

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val doctor = Doctors(
                        id = snapshot.child("id").value?.toString() ?: "",
                        name = snapshot.child("name").value?.toString() ?: "",
                        picture = snapshot.child("picture").value?.toString() ?: "",
                        biography = snapshot.child("biography").value?.toString() ?: "",
                        address = snapshot.child("address").value?.toString() ?: "",
                        special = snapshot.child("special").value?.toString() ?: "",
                        location = snapshot.child("location").value?.toString() ?: "",
                        mobile = snapshot.child("mobile").value?.toString() ?: "",
                        patiens = snapshot.child("patiens").value?.toString() ?: "",
                        site = snapshot.child("site").value?.toString() ?: "",
                        rating = snapshot.child("rating").value?.toString()?.toDoubleOrNull() ?: 0.0,
                        expriense = snapshot.child("expriense").value?.toString()?.toDoubleOrNull() ?: 0.0
                    )
                    onDoctorLoaded(doctor)
                } else {
                    onDoctorLoaded(null) // Handle missing data
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onDoctorLoaded(null) // Handle error case
            }
        })
    }


}
