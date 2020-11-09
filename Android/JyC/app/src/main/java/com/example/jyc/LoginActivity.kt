package com.example.jyc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private  lateinit var forgotPassword: TextView
    private  lateinit var createAccount: TextView
    private  lateinit var txtUserEmail: EditText
    private  lateinit var txtPassword: EditText
    private  lateinit var progressBar: ProgressBar
    private  lateinit var auth: FirebaseAuth
    private  lateinit var btn_Login : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        forgotPassword = findViewById(R.id.forgotPassword)
        createAccount = findViewById(R.id.createAccount)
        txtUserEmail = findViewById(R.id.txtUserEmail)
        txtPassword = findViewById(R.id.txtPassword)
        progressBar = findViewById(R.id.progressBar)
        btn_Login = findViewById(R.id.btn_Login)

        auth = FirebaseAuth.getInstance()

        btn_Login.setOnClickListener {
            loginUser()
        }

        forgotPassword.setOnClickListener {
            startActivity(Intent(this,ForgotPassActivity::class.java))
        }

        createAccount.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }
    }

    private fun loginUser() {
        val user:String=txtUserEmail.text.toString()
        val password:String=txtPassword.text.toString()

        if (!TextUtils.isEmpty(user) && !TextUtils.isEmpty(password)){
            progressBar.visibility = View.VISIBLE
            auth.signInWithEmailAndPassword(user,password).addOnCompleteListener(this){
                task ->
                if (task.isSuccessful){
                    progressBar.visibility = View.INVISIBLE
                    startActivity(Intent(this,MainActivity::class.java))
                }else{
                    Toast.makeText(this,"Error en la autenticación", Toast.LENGTH_LONG).show()
                }
            }

        }
    }


}