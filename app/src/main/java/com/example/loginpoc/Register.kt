package com.example.loginpoc


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Register:AppCompatActivity() {

    val mAuth=FirebaseAuth.getInstance();
    lateinit var mDatabase:DatabaseReference;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val regButton=findViewById<View>(R.id.regButton)as Button
        mDatabase=FirebaseDatabase.getInstance().getReference("Names")

        regButton.setOnClickListener(View.OnClickListener { view ->register()  })
    }
    private fun register(){
        val emailText=findViewById<View>(R.id.emailText)as EditText
        val passwordText=findViewById<View>(R.id.passwordText)as EditText
        val nameText=findViewById<View>(R.id.nameText)as EditText

        var email=emailText.text.toString();
        var password=passwordText.text.toString();
        var name=nameText.text.toString();

        if(!email.isEmpty() && !password.isEmpty() && !name.isEmpty()){
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this,
                OnCompleteListener { task->
                    if(task.isSuccessful){
                        val user=mAuth.currentUser
                        val uid= user!!.uid
                        mDatabase.child(uid).child("Name").setValue(name)
                        Toast.makeText(this,"Successfully signed in",Toast.LENGTH_LONG).show();
                        startActivity(Intent(this,Timeline::class.java))
                    }
                    else{
                        Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()
                    }
                })
        }else{
            Toast.makeText(this,"Please register the credentials",Toast.LENGTH_LONG).show();
        }
    }
}