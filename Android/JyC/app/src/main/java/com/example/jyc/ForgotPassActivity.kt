package com.example.jyc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ForgotPassActivity : AppCompatActivity() {
    private  lateinit var txtEmail: EditText
    private  lateinit var btn_sendEmail : Button
    private  lateinit var auth: FirebaseAuth
    private  lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)
        txtEmail = findViewById(R.id.txtEmail)
        btn_sendEmail = findViewById(R.id.btn_sendEmail)
        progressBar = findViewById(R.id.progressBar)

        auth = FirebaseAuth.getInstance()

        btn_sendEmail.setOnClickListener {
            val email:String = txtEmail.text.toString()
            if (!TextUtils.isEmpty(email)){
                progressBar.visibility = View.VISIBLE
                auth.sendPasswordResetEmail(email).addOnCompleteListener(this){
                    task ->
                    if (task.isSuccessful){
                        startActivity(Intent(this,LoginActivity::class.java))
                    }else{
                        Toast.makeText(this,"Error al enviar email", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}