package com.example.wellcheck.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wellcheck.Domain.CategoryClass
import com.example.wellcheck.Domain.DoctorsModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainViewModel(): ViewModel() {
    private val firebaseDatabase=FirebaseDatabase.getInstance()

    private val _cat = MutableLiveData<MutableList<CategoryClass>>()
    private val _doc= MutableLiveData<MutableList<DoctorsModel>>()
    val cat:LiveData<MutableList<CategoryClass>> = _cat
    val doc:LiveData<MutableList<DoctorsModel>> = _doc

    fun loadCategory(){
        val Ref=firebaseDatabase.getReference("Category")
        Ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               val lists= mutableListOf<CategoryClass>()
                for (childSnapshot in snapshot.children){
                    val list=childSnapshot.getValue(CategoryClass::class.java)
                    if(list!=null){
                        lists.add(list)

                    }
                }
                _cat.value=lists
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    fun loadDoctors(){
        val Ref=firebaseDatabase.getReference("Doctors")
        Ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists= mutableListOf<DoctorsModel>()
                for (childSnapshot in snapshot.children){
                    val list=childSnapshot.getValue(DoctorsModel::class.java)
                    if(list!=null){
                        lists.add(list)

                    }
                }
                _doc.value=lists
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }



}