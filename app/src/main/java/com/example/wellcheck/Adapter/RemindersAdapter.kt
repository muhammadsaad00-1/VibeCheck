package com.example.wellcheck.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wellcheck.Domain.PillReminder
import com.example.wellcheck.R

class RemindersAdapter(private val reminders: List<PillReminder>) :
    RecyclerView.Adapter<RemindersAdapter.ReminderViewHolder>() {

    inner class ReminderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pillName: TextView = itemView.findViewById(R.id.tvPillName)
        val pillDetails: TextView = itemView.findViewById(R.id.tvPillDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reminder, parent, false)
        return ReminderViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        val reminder = reminders[position]
        holder.pillName.text = reminder.pillName
        holder.pillDetails.text = "Quantity: ${reminder.quantity}, " +
                "Duration: ${reminder.duration} days, " +
                "Frequency: ${reminder.frequency}/day"
    }

    override fun getItemCount(): Int = reminders.size
}
