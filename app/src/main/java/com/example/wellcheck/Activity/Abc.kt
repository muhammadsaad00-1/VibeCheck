//package com.example.wellcheck.Activity
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.MenuItem
//import android.view.View
//import android.widget.Toast
//import androidx.appcompat.app.ActionBarDrawerToggle
//import androidx.appcompat.app.AppCompatActivity
//import androidx.drawerlayout.widget.DrawerLayout
//import androidx.lifecycle.Observer
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.wellcheck.Adapter.CategoryAdapter
//import com.example.wellcheck.Adapter.TopDoctorAdapter
//import com.example.wellcheck.R
//import com.example.wellcheck.ViewModel.MainViewModel
//import com.example.wellcheck.databinding.ActivityAbcBinding
//import androidx.core.view.GravityCompat
//import com.google.android.material.navigation.NavigationView
//import com.example.wellcheck.SettingsFragment
//import com.example.wellcheck.ShareFragment
//import com.example.wellcheck.AboutFragment
//
//class Abc : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
//    private lateinit var binding: ActivityAbcBinding
//    private lateinit var drawerLayout: DrawerLayout
//    private val viewModel = MainViewModel()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // Inflate the binding and set the content view
//        binding = ActivityAbcBinding.inflate(layoutInflater)
//        setContentView(binding.root)  // Set the binding root after inflating
//
//        // Initialize categories and top doctors
//        initCategory()
//        initTopDoctors()
//
//        // Setup Drawer Layout
//        drawerLayout = binding.drawerLayout
//        val toolbar = binding.toolbar
//        setSupportActionBar(toolbar)
//
//        val navigationView = binding.navView
//        navigationView.setNavigationItemSelectedListener(this)
//
//        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close)
//        drawerLayout.addDrawerListener(toggle)
//        toggle.syncState()
//
//        // Load the default fragment if there is no saved state
//
//    }
//
//    private fun initTopDoctors() {
//        binding.progressBarTopDoc.visibility = View.VISIBLE
//        viewModel.doc.observe(this, Observer {
//            binding.recycleViewerTopDocs.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//            binding.recycleViewerTopDocs.adapter = TopDoctorAdapter(it)
//            binding.progressBarTopDoc.visibility = View.GONE
//        })
//        viewModel.loadDoctors()
//
//        binding.doctorListTxt.setOnClickListener {
//            startActivity(Intent(this, TopDoctorsActivity::class.java))
//        }
//    }
//
//    private fun initCategory() {
//        binding.progressBarCategory.visibility = View.VISIBLE
//        viewModel.cat.observe(this, Observer {
//            binding.viewCategory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//            binding.viewCategory.adapter = CategoryAdapter(it)
//            binding.progressBarCategory.visibility = View.GONE
//        })
//        viewModel.loadCategory()
//    }
//
//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.nav_home -> {
//                // Handle navigation to Home
//                Toast.makeText(this, "Home selected", Toast.LENGTH_SHORT).show()
//                // Optionally, start a new activity or load a fragment
//            }
//            R.id.nav_settings -> {
//                // Create an Intent to navigate to SearchActivity
//                val intent = Intent(this, SearchActivity::class.java)
//                startActivity(intent)
//
//                // Show a Toast message
//                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show()
//            }
//            R.id.nav_share -> {
//                // Navigate to the ShareFragment
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, ShareFragment())
//                    .commit()
//                Toast.makeText(this, "Share selected", Toast.LENGTH_SHORT).show()
//            }
//            R.id.nav_about -> {
//                // Navigate to the AboutFragment
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, AboutFragment())
//                    .commit()
//                Toast.makeText(this, "About selected", Toast.LENGTH_SHORT).show()
//            }
//        }
//        drawerLayout.closeDrawer(GravityCompat.START)
//        return true
//    }
//
//
//    override fun onBackPressed() {
//        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            drawerLayout.closeDrawer(GravityCompat.START)
//        } else {
//            super.onBackPressed()
//        }
//    }
//}

package com.example.wellcheck.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wellcheck.Adapter.CategoryAdapter
import com.example.wellcheck.Adapter.TopDoctorAdapter
import com.example.wellcheck.Activity.NaviActivity
import com.example.wellcheck.R
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
            startActivity(Intent(this@Abc, NaviActivity::class.java))
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