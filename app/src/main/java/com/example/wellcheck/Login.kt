
package com.example.wellcheck

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.wellcheck.Activity.Intro
import com.google.firebase.auth.FirebaseAuth


class Login : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPass: EditText
    private lateinit var btnLogin : Button
    private lateinit var btnSignUp: Button
    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        mAuth =FirebaseAuth.getInstance()

        edtEmail=findViewById(R.id.edt_email)
        edtPass=findViewById(R.id.edt_pass)
        btnLogin=findViewById(R.id.btn_login)
        btnSignUp=findViewById(R.id.btn_SignUp)

        btnSignUp.setOnClickListener{
            val intent= Intent(this,SignUp::class.java)
            startActivity(intent)

        }
        btnLogin.setOnClickListener {
            val email=edtEmail.text.toString()
            val password=edtPass.text.toString()
            login(email,password)
        }


    }
    private fun login(email: String ,password: String){
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent= Intent(this, TestPage::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@Login,"User don't exist", Toast.LENGTH_SHORT).show()

                }
            }
    }
}
