package com.example.wellcheck.Activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wellcheck.Adapter.BookingsAdapter
import com.example.wellcheck.Domain.Booking
import com.example.wellcheck.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AT : AppCompatActivity() {
    private lateinit var rvBookings: RecyclerView
    private lateinit var tvNoBookings: TextView
    private lateinit var bookingsAdapter: BookingsAdapter
    private val bookingList = mutableListOf<Booking>()
    private val databaseRef = FirebaseDatabase.getInstance().reference.child("appointments")
    private val auth = FirebaseAuth.getInstance()
    private val doctorsRef = FirebaseDatabase.getInstance().reference.child("doctors")

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_at)

        rvBookings = findViewById(R.id.rvBookings)
        tvNoBookings = findViewById(R.id.tvNoBookings)

        rvBookings.layoutManager = LinearLayoutManager(this)
        bookingsAdapter = BookingsAdapter(bookingList,
            onCancel = { booking ->
                AlertDialog.Builder(this)
                    .setTitle("Cancel Appointment")
                    .setMessage("Are you sure you want to cancel this appointment?")
                    .setPositiveButton("Yes") { _, _ ->
                        val bookingId = booking.bookingId ?: return@setPositiveButton
                        val bookingIndex = bookingList.indexOf(booking)

                        // Remove from database
                        databaseRef.child(bookingId).removeValue().addOnSuccessListener {
                            // Remove from list only after success
                            bookingList.removeAt(bookingIndex)
                            bookingsAdapter.notifyItemRemoved(bookingIndex)

                            // Show or hide "No Bookings" message
                            tvNoBookings.visibility = if (bookingList.isEmpty()) View.VISIBLE else View.GONE
                        }.addOnFailureListener {
                            Toast.makeText(this, "Failed to cancel appointment", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("No", null)
                    .show()
            }

            ,
            onReschedule = { booking ->
                if (booking.status == "Pending") {
                    // Show a DatePickerDialog
                    val datePicker = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                        val newDate = "$dayOfMonth-${month + 1}-$year"

                        // Show a TimePickerDialog after selecting the date
                        val timePicker = TimePickerDialog(this, { _, hourOfDay, minute ->
                            val newTime = String.format("%02d:%02d", hourOfDay, minute)

                            // Update both date and time in the database
                            databaseRef.child(booking.bookingId!!).updateChildren(
                                mapOf("date" to newDate, "time" to newTime)
                            ).addOnSuccessListener {
                                booking.date = newDate
                                booking.time = newTime
                                bookingsAdapter.notifyDataSetChanged()
                            }.addOnFailureListener {
                                // Handle failure
                            }
                        }, 12, 0, true)
                        timePicker.show()

                    }, 2024, 0, 1)
                    datePicker.show()
                }
            }
        )
        rvBookings.adapter = bookingsAdapter

        rvBookings.adapter = bookingsAdapter

        loadBookings()
    }
    private fun loadBookings() {
        val patientId = auth.currentUser?.uid ?: return
        databaseRef.orderByChild("patientId").equalTo(patientId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    bookingList.clear()
                    if (snapshot.exists()) {
                        var fetchedBookings = 0
                        val totalBookings = snapshot.childrenCount

                        snapshot.children.forEach { bookingSnapshot ->
                            val booking = bookingSnapshot.getValue(Booking::class.java)
                            booking?.let { b ->
                                // Fetch doctor name and additional details
                                doctorsRef.child(b.doctorId!!).addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(doctorSnapshot: DataSnapshot) {
                                        b.doctorName = doctorSnapshot.child("name").value?.toString() ?: "Unknown Doctor"
                                        // b.doctorSpeciality = doctorSnapshot.child("speciality").value?.toString() ?: "Unknown Speciality"
                                        bookingList.add(b)
                                        fetchedBookings++
                                        checkAndNotify(totalBookings, fetchedBookings)
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        fetchedBookings++
                                        checkAndNotify(totalBookings, fetchedBookings)
                                    }
                                })
                            }
                        }
                    } else {
                        tvNoBookings.visibility = View.VISIBLE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    tvNoBookings.text = "Failed to load bookings."
                    tvNoBookings.visibility = View.VISIBLE
                }
            })
    }

    private fun checkAndNotify(totalBookings: Long, fetchedBookings: Int) {
        if (fetchedBookings.toLong() == totalBookings) {
            bookingsAdapter.notifyDataSetChanged()
            tvNoBookings.visibility = if (bookingList.isEmpty()) View.VISIBLE else View.GONE
        }
    }

}