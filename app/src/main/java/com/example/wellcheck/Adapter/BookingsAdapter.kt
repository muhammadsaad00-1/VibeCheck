package com.example.wellcheck.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wellcheck.Domain.Booking
import com.example.wellcheck.R

class BookingsAdapter(
    private val bookingList: List<Booking>,
    private val onCancel: (Booking) -> Unit,
    private val onReschedule: (Booking) -> Unit
) : RecyclerView.Adapter<BookingsAdapter.BookingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booking, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookingList[position]
        holder.tvDoctorName.text = "Doctor name: ${booking.doctorName}"
        holder.tvDate.text = "Date: ${booking.date}"
        holder.tvTime.text = "Time: ${booking.time}"
        holder.tvStatus.text = "Status: ${booking.status}"

        // Disable buttons if status is not "Pending"
        val isPending = booking.status == "Pending"
        holder.btnCancel.isEnabled = isPending
        holder.btnReschedule.isEnabled = isPending

        // Set click listeners
        holder.btnCancel.setOnClickListener { onCancel(booking) }
        holder.btnReschedule.setOnClickListener { onReschedule(booking) }
    }

    override fun getItemCount(): Int = bookingList.size

    class BookingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDoctorName: TextView = view.findViewById(R.id.tvDoctorName)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val btnCancel: Button = view.findViewById(R.id.btnCancel)
        val btnReschedule: Button = view.findViewById(R.id.btnReschedule)
    }
}

