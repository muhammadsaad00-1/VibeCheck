

package com.example.wellcheck.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wellcheck.Adapter.CategoryAdapter
import com.example.wellcheck.Adapter.TopDoctorAdapter

import com.example.wellcheck.ViewModel.MainViewModel
import com.example.wellcheck.databinding.ActivityAbcBinding

class Abc : AppCompatActivity() {
    private lateinit var binding: ActivityAbcBinding
    private val viewModel = MainViewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivityAbcBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set click listener for the navigation button
        binding.btnNav.setOnClickListener {

            val intent = Intent(this, NaviActivity::class.java)

            startActivity(intent)
        }
        binding.searchi.setOnClickListener{
            startActivity(Intent(this@Abc, SearchActivity::class.java))
        }
        initCategory()
        innitTopDoctors()
    }

    private fun innitTopDoctors() {
        binding.apply {
            progressBarTopDoc.visibility = View.VISIBLE
            viewModel.doc.observe(this@Abc, Observer {
                recycleViewerTopDocs.layoutManager =
                    LinearLayoutManager(this@Abc, LinearLayoutManager.HORIZONTAL, false)
                recycleViewerTopDocs.adapter = TopDoctorAdapter(it)
                progressBarTopDoc.visibility = View.GONE

            })
            viewModel.loadDoctors()
            doctorListTxt.setOnClickListener {
                startActivity(Intent(this@Abc, TopDoctorsActivity::class.java))
            }
        }
    }

    private fun initCategory() {
        binding.progressBarCategory.visibility = View.VISIBLE
        viewModel.cat.observe(this, Observer {
            binding.viewCategory.layoutManager =
                LinearLayoutManager(this@Abc, LinearLayoutManager.HORIZONTAL, false)
            binding.viewCategory.adapter = CategoryAdapter(it)
            binding.progressBarCategory.visibility = View.GONE

        })
        viewModel.loadCategory()
    }
}