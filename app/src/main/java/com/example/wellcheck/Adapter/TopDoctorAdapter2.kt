package com.example.wellcheck.Adapter
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.wellcheck.Activity.DetailActivity
import com.example.wellcheck.Domain.Doctors

import com.example.wellcheck.databinding.ViewholderTopDoctors2Binding
class TopDoctorAdapter2(val items: MutableList<Doctors>): RecyclerView.Adapter<TopDoctorAdapter2.Viewholder>() {
    private var context:Context?= null
    class Viewholder(val binding: ViewholderTopDoctors2Binding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopDoctorAdapter2.Viewholder {
        context=parent.context
        val binding = ViewholderTopDoctors2Binding.inflate(LayoutInflater.from(context), parent,false)
        return Viewholder(binding)
    }
    override fun onBindViewHolder(holder: TopDoctorAdapter2.Viewholder, position: Int) {
        holder.binding.nameTxt.text=items[position].name
        holder.binding.specialTxt.text=items[position].special
        holder.binding.scoreTxt.text=items[position].rating.toString()
        holder.binding.ratingBar.rating=items[position].rating.toFloat()
        Glide.with(holder.itemView.context)
            .load(items[position].picture)
            .apply{RequestOptions().transform(CenterCrop())}
            .into(holder.binding.img)
        holder.binding.makeBtn.setOnClickListener{
            val intent=Intent(context,DetailActivity::class.java)
            intent.putExtra("object",items[position])
            context?.startActivity(intent)
        }
    }
    override fun getItemCount(): Int {
        return items.size
    }
}

