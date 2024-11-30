package com.example.wellcheck.Activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wellcheck.Adapter.AppointmentsAdapter
import com.example.wellcheck.Domain.Booking
import com.example.wellcheck.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DoctorAppointmentsActivity : AppCompatActivity() {

    private lateinit var rvAppointments: RecyclerView
    private lateinit var adapter: AppointmentsAdapter
    private lateinit var database: DatabaseReference
    private var appointmentsList = mutableListOf<Booking>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_appointments)

        rvAppointments = findViewById(R.id.rvAppointments)
        rvAppointments.layoutManager = LinearLayoutManager(this)

        val doctorId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        database = FirebaseDatabase.getInstance().getReference("appointments")

        // Load appointments
        database.orderByChild("doctorId").equalTo(doctorId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    appointmentsList.clear()
                    for (child in snapshot.children) {
                        val appointment = child.getValue(Booking::class.java)
                        appointment?.let { appointmentsList.add(it) }
                    }
                    adapter = AppointmentsAdapter(appointmentsList)
                    rvAppointments.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@DoctorAppointmentsActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
