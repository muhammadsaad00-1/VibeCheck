package com.example.wellcheck.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wellcheck.Domain.CategoryClass
import com.example.wellcheck.databinding.ViewholderCategoryBinding
import com.bumptech.glide.Glide

class CategoryAdapter(val items: MutableList<CategoryClass>) : RecyclerView.Adapter<CategoryAdapter.Viewholder>() {
    private lateinit var context: Context

    // Define the ViewHolder inner class and pass the binding
    inner class Viewholder(val binding: ViewholderCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    // Inflate the view in onCreateViewHolder and return a new ViewHolder instance
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        context = parent.context
        // Inflate the binding
        val binding = ViewholderCategoryBinding.inflate(LayoutInflater.from(context), parent,false)
        return Viewholder(binding)
    }

    // Bind data to the view holder in onBindViewHolder (implementation depends on your data)
    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val item = items[position]
        // Here you can bind data to your views, for example:
        holder.binding.title.text = item.Name
        // Add other bindings as needed
        Glide.with(context)
            .load(item.Picture)
            .into(holder.binding.img)

    }

    // Return the size of your list
    override fun getItemCount(): Int {
        return items.size
    }
}