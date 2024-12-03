package com.example.wellcheck.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wellcheck.Adapter.TopDoctorAdapter2
import com.example.wellcheck.Domain.Doctors
import com.example.wellcheck.Domain.DoctorsModel
import com.example.wellcheck.databinding.ActivitySearchBinding
import com.google.firebase.database.*

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var doctorAdapter: TopDoctorAdapter2
    private var doctorList = mutableListOf<Doctors>()
    private var filteredList = mutableListOf<Doctors>()
    private lateinit var databaseReference: DatabaseReference // Firebase Realtime Database reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView with LinearLayoutManager
        doctorAdapter = TopDoctorAdapter2(filteredList)
        binding.doctorRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.doctorRecyclerView.adapter = doctorAdapter

        // Initialize Firebase Database reference to the "Doctors" node
        databaseReference = FirebaseDatabase.getInstance().getReference("doctors")

        // Fetch doctor data from the Realtime Database
        fetchDoctorsFromDatabase()

        // Set query listener for the SearchView
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterDoctors(newText ?: "")
                return true
            }
        })
    }

    // Fetch doctor list from Firebase Realtime Database
    private fun fetchDoctorsFromDatabase() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                doctorList.clear() // Clear the existing list to avoid duplicates
                for (itemSnapshot in snapshot.children) {
                    val doctor = itemSnapshot.getValue(Doctors::class.java)
                    if (doctor != null) {
                        doctorList.add(doctor)
                    }
                }
                filteredList.clear()
                filteredList.addAll(doctorList) // Initially show all doctors
                doctorAdapter.notifyDataSetChanged() // Update the adapter with initial data
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error getting data: ${error.message}")
            }
        })
    }

    // Filter the doctor list based on the search query
    private fun filterDoctors(query: String) {
        filteredList.clear()
        if (query.isEmpty()) {
            // If the search query is empty, display the full doctor list
            filteredList.addAll(doctorList)
        } else {
            // Filter doctors by the "Special" field
            for (doctor in doctorList) {
                if (doctor.special?.contains(query, ignoreCase = true) == true) {
                    filteredList.add(doctor)
                }
            }
        }
        doctorAdapter.notifyDataSetChanged() // Update the adapter with the filtered data
    }
}
