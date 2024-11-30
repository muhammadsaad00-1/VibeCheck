package com.example.wellcheck.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.wellcheck.Domain.Doctors
import com.example.wellcheck.R
import com.example.wellcheck.west
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class EditDoctorProfile : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var btnSave: Button
    private lateinit var btnUploadImage: Button
    private lateinit var ivProfilePicture: ImageView
    private lateinit var etName: EditText
    private lateinit var etBiography: EditText
    private lateinit var etAddress: EditText
    private lateinit var etSpecial: EditText
    private lateinit var etLocation: EditText
    private lateinit var etMobile: EditText
    private lateinit var etPatients: EditText
    private lateinit var etSite: EditText
    private lateinit var etRating: EditText
    private lateinit var etExperience: EditText
    private lateinit var doctor: Doctors
    private lateinit var goBack : Button
    private lateinit var selectedImageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_doctor_profile)

        auth = FirebaseAuth.getInstance()

        // Initialize UI components
        goBack = findViewById(R.id.backBtn)
        btnSave = findViewById(R.id.btnSave)
        btnUploadImage = findViewById(R.id.btnUploadImage)
        ivProfilePicture = findViewById(R.id.profileImageView)
        etName = findViewById(R.id.nameEditText)
        etBiography = findViewById(R.id.bioEditText)
        etAddress = findViewById(R.id.addressEditText)
        etSpecial = findViewById(R.id.specialtyEditText)
        etLocation = findViewById(R.id.loci)
        etMobile = findViewById(R.id.phoneEditText)
        etPatients = findViewById(R.id.patientsEditText)
        etSite = findViewById(R.id.websiteEditText)
        etRating = findViewById(R.id.ratingEditText)
        etExperience = findViewById(R.id.experienceEditText)

        // Get doctor object passed from the previous activity
        doctor = intent.getParcelableExtra("doctor") ?: return

        // Set existing data to the fields
        etName.setText(doctor.name)
        etBiography.setText(doctor.biography)
        etAddress.setText(doctor.address)
        etSpecial.setText(doctor.special)
        etLocation.setText(doctor.location)
        etMobile.setText(doctor.mobile)
        etPatients.setText(doctor.patiens)
        etSite.setText(doctor.site)
        etRating.setText(doctor.rating.toString())
        etExperience.setText(doctor.expriense.toString())

        Glide.with(this).load(doctor.picture).into(ivProfilePicture)

        // Image upload functionality using ActivityResultContracts
        btnUploadImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            imagePickerLauncher.launch(intent)
        }
        goBack.setOnClickListener{
            val intent = Intent(this, west::class.java)
            startActivity(intent)
            finish()
        }
        // Save button functionality
        btnSave.setOnClickListener {
            // If image is selected, upload it to Firebase Storage and get the URL
            if (::selectedImageUri.isInitialized) {
                val fileName = UUID.randomUUID().toString()
                val storageRef = FirebaseStorage.getInstance().reference.child("profile_pictures/$fileName")

                storageRef.putFile(selectedImageUri).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        storageRef.downloadUrl.addOnSuccessListener { uri ->
                            val updatedDoctor = doctor.copy(
                                name = etName.text.toString(),
                                biography = etBiography.text.toString(),
                                address = etAddress.text.toString(),
                                special = etSpecial.text.toString(),
                                location = etLocation.text.toString(),
                                mobile = etMobile.text.toString(),
                                patiens = etPatients.text.toString(),
                                site = etSite.text.toString(),
                                rating = etRating.text.toString().toDouble(),
                                expriense = etExperience.text.toString().toDouble(),
                                picture = uri.toString() // Save image URL
                            )

                            saveDoctorProfile(updatedDoctor)
                        }
                    } else {
                        Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                val updatedDoctor = doctor.copy(
                    name = etName.text.toString(),
                    biography = etBiography.text.toString(),
                    address = etAddress.text.toString(),
                    special = etSpecial.text.toString(),
                    location = etLocation.text.toString(),
                    mobile = etMobile.text.toString(),
                    patiens = etPatients.text.toString(),
                    site = etSite.text.toString(),
                    rating = etRating.text.toString().toDouble(),
                    expriense = etExperience.text.toString().toDouble()
                )

                saveDoctorProfile(updatedDoctor)
            }
        }
    }

    // Method to save the updated doctor profile in Firebase Database
    private fun saveDoctorProfile(updatedDoctor: Doctors) {
        val ref = FirebaseDatabase.getInstance().reference.child("doctors").child(auth.currentUser?.uid ?: "")
        ref.setValue(updatedDoctor).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                onBackPressed() // Go back to WestActivity
            } else {
                Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Handle image picker result
    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                selectedImageUri = result.data?.data ?: return@registerForActivityResult
                ivProfilePicture.setImageURI(selectedImageUri) // Set image to ImageView
            }
        }

    // Handle back button press

}
