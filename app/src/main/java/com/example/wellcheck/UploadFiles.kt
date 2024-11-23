package com.example.wellcheck

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wellcheck.Domain.FileModel
import com.example.wellcheck.adapter.FileAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class UploadFiles : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var uploadButton: Button
    private lateinit var fileAdapter: FileAdapter
    private val fileList = mutableListOf<FileModel>()
    private lateinit var auth: FirebaseAuth
    private val PICK_FILE_REQUEST = 1
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_files)

        // Initialize Firebase Auth, Storage, and Database
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()

        // Initialize views
        recyclerView = findViewById(R.id.recyclerViewFiles)
        uploadButton = findViewById(R.id.btnUploadFile)
        val backButton: Button = findViewById(R.id.btnBack)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        fileAdapter = FileAdapter(fileList,
            onFileClick = { file -> openFile(file.fileUrl) },
            onDownloadClick = { file -> downloadFile(file.fileUrl) }
        )
        recyclerView.adapter = fileAdapter

        // Fetch files from Firebase
        fetchFiles()

        // Handle upload button click
        uploadButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*" // Allow all types of files
            startActivityForResult(intent, PICK_FILE_REQUEST)
        }

        // Back button to navigate to TestPage
        backButton.setOnClickListener {
            startActivity(Intent(this, TestPage::class.java))
        }
    }
    override fun onResume() {
        super.onResume()
        fetchFiles() // Ensure files are fetched whenever the activity is resumed
    }


    private fun openFile(fileUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(fileUrl))
        startActivity(intent)
    }

    private fun downloadFile(fileUrl: String) {
        // Implement download logic or show a message
        Toast.makeText(this, "Downloading $fileUrl", Toast.LENGTH_SHORT).show()
    }

    private fun fetchFiles() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val storageRef = FirebaseStorage.getInstance().reference.child("medical_reports/$userId")

            storageRef.listAll()
                .addOnSuccessListener { result ->
                    val fileList = mutableListOf<FileModel>()

                    result.items.forEach { storageFile ->
                        // Retrieve metadata for each file
                        storageFile.metadata.addOnSuccessListener { metadata ->
                            val fileName = metadata.name
                            val uploadDate = metadata.creationTimeMillis?.let {
                                java.text.SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault()).format(it)
                            } ?: "Unknown Date"
                            val note = metadata.getCustomMetadata("note")
                            val title = metadata.getCustomMetadata("title")


                            // Generate download URL for the file
                            storageFile.downloadUrl.addOnSuccessListener { downloadUrl ->
                                fileList.add(
                                    FileModel(
                                        fileName = fileName.toString(),
                                        fileUrl = downloadUrl.toString(),
                                        title = title,
                                        uploadDate = uploadDate,
                                        note = note
                                    )
                                )

                                // Update adapter once all files are loaded
                                if (fileList.size == result.items.size) {
                                    fileAdapter.updateFiles(fileList)
                                }
                            }.addOnFailureListener {
                                android.util.Log.e("FileAdapter", "Error retrieving download URL: ${it.message}")
                            }
                        }.addOnFailureListener {
                            android.util.Log.e("FileAdapter", "Error retrieving metadata: ${it.message}")
                        }
                    }
                }
                .addOnFailureListener { e ->
                    android.util.Log.e("FileAdapter", "Error listing files: ${e.message}")
                }
        } else {
            android.util.Log.e("FileAdapter", "User not authenticated.")
        }

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_FILE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val fileUri: Uri? = data.data
            if (fileUri != null) {
                uploadFile(fileUri) // Upload the selected file
            }
        }
    }

    private fun uploadFile(fileUri: Uri) {
        val uid = auth.currentUser?.uid ?: return
        val fileName = System.currentTimeMillis().toString()
        val storageRef = storage.reference.child("medical_reports/$uid/$fileName")

        // Show an input dialog to get file title and note from user
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.dialog_file_upload, null)
        val titleEditText = view.findViewById<EditText>(R.id.editFileTitle)
        val noteEditText = view.findViewById<EditText>(R.id.editFileNote)

        builder.setView(view)
        builder.setPositiveButton("Upload") { dialog, which ->
            val title = titleEditText.text.toString()
            val note = noteEditText.text.toString()
            val uploadDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

            // Prepare metadata
            val metadata = com.google.firebase.storage.StorageMetadata.Builder()
                .setCustomMetadata("title", title)
                .setCustomMetadata("note", note)
                .build()

            // Upload file with metadata
            storageRef.putFile(fileUri, metadata).addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { fileUrl ->
                    // Save file data in Firebase Realtime Database
                    val ref = database.reference.child("patients").child(uid).child("medicalHistory")
                    val file = FileModel(fileName, fileUrl.toString(), title, uploadDate, note)
                    ref.child(fileName).setValue(file).addOnCompleteListener {
                        if (it.isSuccessful) {
                            fileList.add(file) // Add the uploaded file to the list
                            fileAdapter.notifyDataSetChanged() // Notify adapter to update UI
                            Toast.makeText(this, "File uploaded successfully!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Failed to save file info!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "File upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel", null)
        builder.create().show()
    }

}
