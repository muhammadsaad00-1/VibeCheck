package com.example.wellcheck.Activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wellcheck.Adapter.HealthTipsAdapter
import com.example.wellcheck.Domain.HealthTip
import com.example.wellcheck.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PostTipActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var healthTipsAdapter: HealthTipsAdapter
    private lateinit var healthTipsList: MutableList<HealthTip>

    private val database = FirebaseDatabase.getInstance()
    private val healthTipsRef = database.getReference("healthTips")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_tip)

        recyclerView = findViewById(R.id.tipsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this) // Use 'this' for context

        // Initialize the list of health tips
        healthTipsList = mutableListOf()

        // Set the adapter for the RecyclerView
        healthTipsAdapter = HealthTipsAdapter(healthTipsList)
        recyclerView.adapter = healthTipsAdapter

        // Fetch the health tips from Firebase
        fetchHealthTips()

        // Post a new health tip when the button is clicked
        findViewById<Button>(R.id.postTipButton).setOnClickListener {
            val tipText = findViewById<EditText>(R.id.tipEditText).text.toString()
            if (tipText.isNotEmpty()) {
                postNewTip(tipText)
            } else {
                Toast.makeText(this, "Please enter a tip", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to fetch health tips from Firebase
    private fun fetchHealthTips() {
        healthTipsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                healthTipsList.clear()
                for (tipSnapshot in snapshot.children) {
                    val tip = tipSnapshot.getValue(HealthTip::class.java)
                    tip?.let { healthTipsList.add(it) }
                }
                healthTipsAdapter.notifyDataSetChanged() // Refresh the RecyclerView
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("PostTipActivity", "Failed to read health tips", error.toException())
            }
        })
    }

    // Function to post a new health tip to Firebase
    private fun postNewTip(tipText: String) {
        val newTip = HealthTip(tipText, System.currentTimeMillis())
        healthTipsRef.push().setValue(newTip)
            .addOnSuccessListener {
                Toast.makeText(this, "Tip posted successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to post tip", Toast.LENGTH_SHORT).show()
            }
    }
}