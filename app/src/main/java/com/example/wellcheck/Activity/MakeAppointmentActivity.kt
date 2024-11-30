package com.example.wellcheck.Activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wellcheck.Domain.Booking
import com.example.wellcheck.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class MakeAppointmentActivity : AppCompatActivity() {

    private lateinit var btnSelectDate: Button
    private lateinit var btnSelectTime: Button
    private lateinit var btnMakeAppointment: Button
    private lateinit var tvSelectDate: TextView
    private lateinit var tvSelectTime: TextView

    private var selectedDate: String? = null
    private var selectedTime: String? = null
    private lateinit var patientId: String
    private lateinit var doctorId: String
    private lateinit var doctorName: String // Pass this from the previous activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_appointment)

        // Initialize views
        btnSelectDate = findViewById(R.id.btnSelectDate)
        btnSelectTime = findViewById(R.id.btnSelectTime)
        btnMakeAppointment = findViewById(R.id.btnMakeAppointment)
        tvSelectDate = findViewById(R.id.tvSelectDate)
        tvSelectTime = findViewById(R.id.tvSelectTime)

        // Get doctorId from intent
        doctorId = intent.getStringExtra("doctorId") ?: ""
        doctorName = intent.getStringExtra("doctorName") ?: ""
        patientId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        // Date picker
        btnSelectDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = "$selectedDay-${selectedMonth + 1}-$selectedYear"
                tvSelectDate.text = "Selected Date: $selectedDate"
            }, year, month, day).show()
        }

        // Time picker
        btnSelectTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                tvSelectTime.text = "Selected Time: $selectedTime"
            }, hour, minute, true).show()
        }

        // Make appointment
        btnMakeAppointment.setOnClickListener {
            if (selectedDate == null || selectedTime == null) {
                Toast.makeText(this, "Please select date and time", Toast.LENGTH_SHORT).show()
            } else {
                makeAppointment()
            }
        }
    }

    private fun makeAppointment() {
        val database = FirebaseDatabase.getInstance()
        val bookingId = database.reference.push().key ?: return

        val booking = Booking(
            bookingId = bookingId,
            patientId = patientId,
            doctorId = doctorId,
            date = selectedDate!!,
            time = selectedTime!!,
            doctorName = doctorName // Include doctor name
        )

        database.reference.child("appointments").child(bookingId)
            .setValue(booking)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Appointment booked successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to book appointment", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
