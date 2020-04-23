package com.example.loginpoc


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity()
{
    val mAuth= FirebaseAuth.getInstance();
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginButton=findViewById<View>(R.id.loginButton) as Button
        val regText=findViewById<View>(R.id.regText)as TextView

        loginButton.setOnClickListener(View.OnClickListener {
                view->login()
        })
        regText.setOnClickListener(View.OnClickListener {
                view ->register()
        })
    }
    private fun register(){
        startActivity(Intent(this,Register::class.java))
    }
    private fun login()
    {
        val emailText=findViewById<View>(R.id.emailText)as EditText
        val passwordText=findViewById<View>(R.id.passwordText)as EditText

        var email=emailText.text.toString();
        var password=passwordText.text.toString();

        if(!email.isEmpty() && !password.isEmpty())
        {
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, OnCompleteListener { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, Timeline::class.java))
                    Toast.makeText(this, "successfully logid in", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                }
            })
        }
        else{
            Toast.makeText(this,"Please Enter the credentials",Toast.LENGTH_LONG).show();
        }
    }
}


