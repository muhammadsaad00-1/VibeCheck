package com.example.wellcheck.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.wellcheck.Domain.HealthTip
import com.example.wellcheck.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HealthTipsAdapter(private val healthTipsList: List<HealthTip>) :
    RecyclerView.Adapter<HealthTipsAdapter.HealthTipViewHolder>() {

    // ViewHolder class to hold UI elements for each item
    inner class HealthTipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tipText: TextView = itemView.findViewById(R.id.tipText)
        val timestampText: TextView = itemView.findViewById(R.id.timestampText)
    }

    // Create a new view holder for each item in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HealthTipViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_health_tip, parent, false)
        return HealthTipViewHolder(view)
    }

    // Bind the data to the views in each item of the RecyclerView
    override fun onBindViewHolder(holder: HealthTipViewHolder, position: Int) {
        val healthTip = healthTipsList[position]
        holder.tipText.text = healthTip.text
        holder.timestampText.text = "Posted on: ${SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(
            Date(healthTip.timestamp!!)
        )}"
    }

    override fun getItemCount(): Int {
        return healthTipsList.size
    }
}
