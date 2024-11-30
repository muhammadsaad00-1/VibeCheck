package com.example.wellcheck.Activity
import com.example.wellcheck.R
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.wellcheck.databinding.ActivityDonateBinding

class Donate : AppCompatActivity() {

    private lateinit var binding: ActivityDonateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_donate)
        // Initialize View Binding
        binding = ActivityDonateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle the Donate button click event
        binding.btnDonate.setOnClickListener {
            // Toggle visibility of the details layout
            if (binding.detailsLayout.visibility == View.GONE) {
                binding.detailsLayout.visibility = View.VISIBLE
                binding.btnDonate.text = "Hide Details"  // Change button text after showing the details
            } else {
                binding.detailsLayout.visibility = View.GONE
                binding.btnDonate.text = "Donate Now"  // Change button text back when hiding details
            }
        }
    }
}
