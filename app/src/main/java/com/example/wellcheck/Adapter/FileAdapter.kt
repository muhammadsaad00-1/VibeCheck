package com.example.wellcheck.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wellcheck.R
import com.example.wellcheck.Domain.FileModel

class FileAdapter(
    private var files: MutableList<FileModel>,
    private val onFileClick: (FileModel) -> Unit,
    private val onDownloadClick: (FileModel) -> Unit
) : RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false)
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val file = files[position]
        holder.bind(file)
    }

    override fun getItemCount(): Int = files.size

    fun updateFiles(newFiles: List<FileModel>) {
        files.clear()
        files.addAll(newFiles)
        notifyDataSetChanged() // Ensure complete list update
    }

    inner class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val fileTitleTextView: TextView = itemView.findViewById(R.id.txtFileTitle)
        private val uploadDateTextView: TextView = itemView.findViewById(R.id.txtUploadDate)
        private val fileNoteTextView: TextView = itemView.findViewById(R.id.txtFileNote)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onFileClick(files[position])
                }
            }

            itemView.findViewById<ImageView>(R.id.imgDownload).setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDownloadClick(files[position])
                }
            }
        }

        fun bind(file: FileModel) {
            fileTitleTextView.text = file.title ?: "No Title" // Fallback if title is null
            uploadDateTextView.text = "Uploaded on: ${file.uploadDate ?: "Unknown Date"}"
            fileNoteTextView.text = file.note ?: "No Notes" // Fallback if note is null
        }

    }
}
