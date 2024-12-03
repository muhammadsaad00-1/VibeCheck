package com.example.wellcheck.Activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wellcheck.Adapter.RemindersAdapter
import com.example.wellcheck.Domain.PillReminder
import com.example.wellcheck.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ViewRemindersActivity : AppCompatActivity() {

    private lateinit var rvReminders: RecyclerView
    private lateinit var remindersAdapter: RemindersAdapter
    private lateinit var pillRemindersList: MutableList<PillReminder> // List to hold pill reminders

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_reminders)

        // Initialize RecyclerView and list
        rvReminders = findViewById(R.id.rvReminders)
        rvReminders.layoutManager = LinearLayoutManager(this)
        pillRemindersList = mutableListOf()
        remindersAdapter = RemindersAdapter(pillRemindersList)

        rvReminders.adapter = remindersAdapter

        // Fetch pill reminders for the current user
        fetchPillReminders()
    }

    private fun fetchPillReminders() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            // Reference to the pill reminders node in Firebase
            val ref = FirebaseDatabase.getInstance().getReference("patients").child(userId).child("pillReminders")

            ref.get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    // Clear the current list and populate it with new data
                    pillRemindersList.clear()

                    // Iterate over the snapshot and retrieve the pill reminder details
                    for (childSnapshot in snapshot.children) {
                        val pillReminder = childSnapshot.getValue(PillReminder::class.java)
                        if (pillReminder != null) {
                            pillRemindersList.add(pillReminder)
                        }
                    }

                    // Update the adapter once data is fetched
                    remindersAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "No reminders found.", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching reminders: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "User not authenticated.", Toast.LENGTH_SHORT).show()
        }
    }
}