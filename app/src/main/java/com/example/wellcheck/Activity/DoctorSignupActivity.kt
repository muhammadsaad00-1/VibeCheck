package com.example.wellcheck.Activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

import com.example.wellcheck.Domain.Doctors
import com.example.wellcheck.PartialLogin
import com.example.wellcheck.R

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class DoctorSignupActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etBiography: EditText
    private lateinit var etAddress: EditText
    private lateinit var spSpecialization: Spinner

    private lateinit var etLocation: EditText
    private lateinit var etMobile: EditText
    private lateinit var etPatients: EditText
    private lateinit var etSite: EditText
    private lateinit var etRating: EditText
    private lateinit var etExperience: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignup: Button
    private lateinit var btnUploadPicture: Button
    private lateinit var ivProfilePicture: ImageView

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().getReference("doctors")
    private val storage = FirebaseStorage.getInstance().getReference("doctor_pictures")

    private var imageUri: Uri? = null

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_signup)

        // Initialize UI components
        etName = findViewById(R.id.etName)
        etBiography = findViewById(R.id.etBiography)
        etAddress = findViewById(R.id.etAddress)
        spSpecialization = findViewById(R.id.spSpecialization)

        etLocation = findViewById(R.id.etLocation)
        etMobile = findViewById(R.id.etMobile)
        etPatients = findViewById(R.id.etPatients)
        etSite = findViewById(R.id.etSite)
        etRating = findViewById(R.id.etRating)
        etExperience = findViewById(R.id.etExperience)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnSignup = findViewById(R.id.btnSignup)
        btnUploadPicture = findViewById(R.id.btnUploadPicture)
        ivProfilePicture = findViewById(R.id.ivProfilePicture)

        btnUploadPicture.setOnClickListener { openImageChooser() }
        btnSignup.setOnClickListener { registerDoctor() }
    }

    private fun openImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            ivProfilePicture.setImageURI(imageUri)
        }
    }

    private fun registerDoctor() {
        val name = etName.text.toString()
        val biography = etBiography.text.toString()
        val address = etAddress.text.toString()
        val special = spSpecialization.selectedItem.toString()
        val location = etLocation.text.toString()
        val mobile = etMobile.text.toString()
        val patients = etPatients.text.toString()
        val site = etSite.text.toString()
        val rating = etRating.text.toString().toDoubleOrNull() ?: 0.0
        val experience = etExperience.text.toString().toDoubleOrNull() ?: 0.0
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill all fields and upload a picture!", Toast.LENGTH_SHORT).show()
            return
        }
        if (special.isEmpty() || special == "Specialization") {
            Toast.makeText(this, "Please select a specialization", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val doctorId = auth.currentUser?.uid ?: ""
                    uploadImageToFirebaseStorage(
                        doctorId, name, biography, address, special, location, mobile,
                        patients, site, rating, experience
                    )
                } else {
                    Toast.makeText(this, "Signup failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun uploadImageToFirebaseStorage(
        doctorId: String, name: String, biography: String, address: String,
        special: String, location: String, mobile: String, patients: String,
        site: String, rating: Double, experience: Double
    ) {
        val fileReference = storage.child("${UUID.randomUUID()}.jpg")
        fileReference.putFile(imageUri!!)
            .addOnSuccessListener {
                fileReference.downloadUrl.addOnSuccessListener { uri ->
                    saveDoctorToDatabase(
                        doctorId, name, uri.toString(), biography, address, special, location,
                        mobile, patients, site, rating, experience
                    )
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Image upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveDoctorToDatabase(
        doctorId: String, name: String, pictureUrl: String, biography: String, address: String,
        special: String, location: String, mobile: String, patients: String,
        site: String, rating: Double, experience: Double
    ) {
        val doctor = Doctors(
            id = doctorId,
            name = name,
            picture = pictureUrl,
            biography = biography,
            address = address,
            special = special,
            location = location,
            mobile = mobile,
            patiens = patients,
            site = site,
            rating = rating,
            expriense = experience
        )

        database.child(doctorId).setValue(doctor)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, PartialLogin::class.java) // Directly navigate to the doctor details page
                    intent.putExtra("doctor", doctor) // Pass doctor data to WestActivity
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Failed to save doctor: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}