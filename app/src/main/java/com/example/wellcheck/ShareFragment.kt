package com.example.wellcheck

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wellcheck.Adapter.HealthTipsAdapter
import com.example.wellcheck.Adapter.TopDoctorAdapter
import com.example.wellcheck.Domain.HealthTip
import com.example.wellcheck.R
import com.example.wellcheck.ViewModel.MainViewModel
import com.example.wellcheck.databinding.ActivityTopDoctorsBinding
import com.example.wellcheck.databinding.FragmentShareBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShareFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShareFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var healthTipsAdapter: HealthTipsAdapter
    private lateinit var healthTipsList: MutableList<HealthTip>

    private val database = FirebaseDatabase.getInstance()
    private val healthTipsRef = database.getReference("healthTips")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentShareBinding.inflate(inflater, container, false)

        recyclerView = binding.root.findViewById(R.id.tipsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize the list of health tips
        healthTipsList = mutableListOf()

        // Set the adapter for the RecyclerView
        healthTipsAdapter = HealthTipsAdapter(healthTipsList)
        recyclerView.adapter = healthTipsAdapter

        // Fetch the health tips from Firebase
        fetchHealthTips()

        // Post a new health tip when the button is clicked
        binding.root.findViewById<Button>(R.id.postTipButton).setOnClickListener {
            val tipText = binding.root.findViewById<EditText>(R.id.tipEditText).text.toString()
            if (tipText.isNotEmpty()) {
                postNewTip(tipText)
            } else {
                Toast.makeText(context, "Please enter a tip", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
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
                Log.w("ShareFragment", "Failed to read health tips", error.toException())
            }
        })
    }

    // Function to post a new health tip to Firebase
    private fun postNewTip(tipText: String) {
        val newTip = HealthTip(tipText, System.currentTimeMillis())
        healthTipsRef.push().setValue(newTip)
            .addOnSuccessListener {
                Toast.makeText(context, "Tip posted successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to post tip", Toast.LENGTH_SHORT).show()
            }
    }
}
