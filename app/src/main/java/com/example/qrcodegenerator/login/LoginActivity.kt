package com.example.qrcodegenerator.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.qrcodegenerator.EmployeeStatusActivity
import com.example.qrcodegenerator.R
import com.example.qrcodegenerator.signUp.SignUpActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    lateinit var etLoginEmail      : TextInputEditText
    lateinit var etLoginPassword   : TextInputEditText
    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        etLoginEmail    = findViewById(R.id.etLoginEmail)
        etLoginPassword = findViewById(R.id.etLoginPassword)

        mAuth = FirebaseAuth.getInstance()

        findViewById<Button>(R.id.btnLogin).setOnClickListener(View.OnClickListener { loginUser() })

        findViewById<Button>(R.id.btnSignUp).setOnClickListener(View.OnClickListener {
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        })

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        if(currentUser != null){
            val intent = Intent(this,EmployeeStatusActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun loginUser()
    {
        val loginEmail: String = etLoginEmail.text.toString().trim()
        val loginPassword : String = etLoginPassword.text.toString().trim()

        if(loginEmail.equals(""))
        {
         etLoginEmail.setError("Enter Email")
        }
        else if (loginPassword.equals(""))
        {
            etLoginPassword.setError("Enter Password")
        }
        else
        {
            mAuth.signInWithEmailAndPassword(loginEmail,loginPassword).addOnCompleteListener {task->

                if(task.isSuccessful)
                {
                    val intent = Intent(this,EmployeeStatusActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
                else
                {
                    Toast.makeText(this, "Error Message" + task.exception!!.message, Toast.LENGTH_SHORT).show()

                }

            }
        }

    }


}