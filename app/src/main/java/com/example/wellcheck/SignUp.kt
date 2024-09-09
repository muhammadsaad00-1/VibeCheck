package com.example.wellcheck

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUp : AppCompatActivity() {
    private lateinit var edtName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPass: EditText

    private lateinit var btnSignUp: Button
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sign_up)
        auth =FirebaseAuth.getInstance()
        edtName=findViewById(R.id.name)
        edtEmail=findViewById(R.id.email)
        edtPass=findViewById(R.id.pass)

        btnSignUp=findViewById(R.id.btn_signUp)

        btnSignUp.setOnClickListener{
            val email= edtEmail.text.toString()
            val pass =edtPass.text.toString()

            signedUp(email,pass)

        }
    }



    private fun signedUp(email:String, pass:String){

        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
//jump to home
                    val intent= Intent(this,Intro::class.java)
                    startActivity(intent)
                } else {
               Toast.makeText(this@SignUp,"Some Error occured",Toast.LENGTH_SHORT).show()
                }
            }
    }
}