package com.example.jyc

import MyResources.Storage
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

// Actividad encargadar de redireccionar a las actividades,
// registro , reseteo de contraseña o logear al usuario en la aplicacion

class LoginActivity : AppCompatActivity() {
    private var pressedTime: Long = 0

    private lateinit var forgotPassword: TextView
    private lateinit var createAccount: TextView
    private lateinit var txtUserEmail: EditText
    private lateinit var txtPassword: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth
    private lateinit var btn_Login: Button
    private lateinit var service: Storage

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
        service = Storage()

        btn_Login.setOnClickListener {
            loginUser()
        }

        forgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPassActivity::class.java))
        }

        createAccount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
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

    // Metodo para autenticar  en firebase
    fun authSign(user: String?, password: String?) {
        if (user != null && password != null) {
            auth.signInWithEmailAndPassword(user, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    progressBar.visibility = View.INVISIBLE

                    // Si la autenticacion es satisfactoria guardamos las credenciales
                    // para no tener que autenticar nuevamente en el futuro
                    service.setPreferenceKey(this, "user", user)
                    service.setPreferenceKey(this, "password", password)

                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    showAler("Error", "Error en la autenticación")
                }
            }
        }
    }

    // Sobrescribimos el metodo que la salida de la aplicacion sea mas agil
    // de no ser asi volverimos a la actividad Splash y tendriamos que salir desde ahi
    override fun onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.finishAffinity()
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }

        pressedTime = System.currentTimeMillis();
    }

}

