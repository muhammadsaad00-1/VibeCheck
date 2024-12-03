package com.example.wellcheck

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.wellcheck.Activity.Abc
import com.example.wellcheck.Activity.Fpass
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPass: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: Button
    private lateinit var btnForgetPassword: Button
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        edtEmail = findViewById(R.id.edt_email)
        edtPass = findViewById(R.id.edt_pass)
        btnLogin = findViewById(R.id.btn_login)
        btnSignUp = findViewById(R.id.btn_SignUp)
        btnForgetPassword = findViewById(R.id.btn_Forget_password)

        btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPass.text.toString()
            login(email, password)
        }

        btnForgetPassword.setOnClickListener {
            val intent = Intent(this, Fpass::class.java)
            startActivity(intent)
            val email = edtEmail.text.toString()
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email to reset the password", Toast.LENGTH_SHORT).show()
            } else {
                resetPassword(email)
            }
        }
    }

    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, Abc::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@Login, "User doesn't exist", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun resetPassword(email: String) {
        mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Password reset email sent to $email", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Fpass::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Error in sending reset email", Toast.LENGTH_SHORT).show()
                }
            }
    }
}