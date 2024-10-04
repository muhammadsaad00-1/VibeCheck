package com.example.wellcheck.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.wellcheck.databinding.ViewholderTopDoctorBinding
import com.example.wellcheck.Domain.DoctorsModel


class TopDoctorAdapter(val items: MutableList<DoctorsModel>): RecyclerView.Adapter<TopDoctorAdapter.Viewholder>() {

    private var context:Context?= null
     class Viewholder(val binding: ViewholderTopDoctorBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopDoctorAdapter.Viewholder {
        context=parent.context
        val binding = ViewholderTopDoctorBinding.inflate(LayoutInflater.from(context), parent,false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: TopDoctorAdapter.Viewholder, position: Int) {
        holder.binding.nameTxt.text=items[position].Name
         holder.binding.specialTxt.text=items[position].Special
        holder.binding.scoreTxt.text=items[position].Rating.toString()
        holder.binding.yearTxt.text=items[position].Exprience.toString()+"Year"

        Glide.with(holder.itemView.context)
            .load(items[position].Picture)
            .apply{RequestOptions().transform(CenterCrop())}
            .into(holder.binding.img)


    }

    override fun getItemCount(): Int {
        return items.size
    }
}