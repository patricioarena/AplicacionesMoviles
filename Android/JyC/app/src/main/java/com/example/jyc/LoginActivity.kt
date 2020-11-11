package com.example.jyc

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception


class LoginActivity : AppCompatActivity() {

    private lateinit var forgotPassword: TextView
    private lateinit var createAccount: TextView
    private lateinit var txtUserEmail: EditText
    private lateinit var txtPassword: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth
    private lateinit var btn_Login: Button
    private var preferenceKey: String = ""

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
            startActivity(Intent(this, ForgotPassActivity::class.java))
        }

        createAccount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        try {
            var aUser = getPreferenceKey(this, "user")
            var aPass = getPreferenceKey(this, "password")

            if (!TextUtils.isEmpty(aUser)) {
                authSign(aUser, aPass)
            }

        } catch (e: Exception) {
            showAler(e.localizedMessage.toString(), e.message.toString())
        }


    }

    private fun loginUser() {
        val user: String = txtUserEmail.text.toString()
        val password: String = txtPassword.text.toString()

        if (!TextUtils.isEmpty(user) && !TextUtils.isEmpty(password)) {
            progressBar.visibility = View.VISIBLE
            authSign(user, password)
        }
    }

    private fun showAler(title: String?, message: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun setPreferenceKey(context: Context, keyPref: String?, valor: String?) {
        val settings = context.getSharedPreferences(preferenceKey, Context.MODE_PRIVATE)
        val editor: Editor
        editor = settings.edit()
        editor.putString(keyPref, valor)
        editor.commit()
    }

    fun getPreferenceKey(context: Context, keyPref: String?): String? {
        val preferences = context.getSharedPreferences(preferenceKey, Context.MODE_PRIVATE)
        return preferences.getString(keyPref, "")
    }

    fun authSign(user: String?, password: String?) {
        if (user != null && password != null) {
            auth.signInWithEmailAndPassword(user, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    progressBar.visibility = View.INVISIBLE

                    setPreferenceKey(this, "user", user)
                    setPreferenceKey(this, "password", password)

                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    showAler("Error", "Error en la autenticación")
                }
            }
        }
    }
}