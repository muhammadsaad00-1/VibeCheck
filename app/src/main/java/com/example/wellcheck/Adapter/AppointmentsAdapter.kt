package com.example.wellcheck.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wellcheck.Domain.Booking
import com.example.wellcheck.R
import com.google.firebase.database.FirebaseDatabase

class AppointmentsAdapter(private val appointmentsList: List<Booking>) :
    RecyclerView.Adapter<AppointmentsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPatientId: TextView = itemView.findViewById(R.id.tvPatientId)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val btnAccept: Button = itemView.findViewById(R.id.btnAccept)
        val btnDeny: Button = itemView.findViewById(R.id.btnDeny)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_appointment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appointment = appointmentsList[position]

        holder.tvPatientId.text = "Patient ID: ${appointment.patientId}"
        holder.tvDate.text = "Date: ${appointment.date}"
        holder.tvTime.text = "Time: ${appointment.time}"

        // Set background color based on status
        when (appointment.status) {
            "Accepted" -> holder.itemView.setBackgroundColor(holder.itemView.context.getColor(R.color.light_green))
            "Denied" -> holder.itemView.setBackgroundColor(holder.itemView.context.getColor(R.color.light_red))
            else -> holder.itemView.setBackgroundColor(holder.itemView.context.getColor(R.color.white))
        }

        // Handle Accept Button
        holder.btnAccept.setOnClickListener {
            updateAppointmentStatus(appointment.bookingId, "Accepted")
        }

        // Handle Deny Button
        holder.btnDeny.setOnClickListener {
            updateAppointmentStatus(appointment.bookingId, "Denied")
        }
    }

    private fun updateAppointmentStatus(bookingId: String, status: String) {
        val database = FirebaseDatabase.getInstance().getReference("appointments")
        database.child(bookingId).child("status").setValue(status)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Reflect changes in real-time
                    notifyDataSetChanged()
                }
            }
    }


    override fun getItemCount(): Int = appointmentsList.size


}
