package com.example.jyc

import Models.Domicilio
import Models.UserDb
import MyResources.DataBaseHelper
import MyResources.Facade
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson


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
    private var db = FirebaseFirestore.getInstance()
    private lateinit var btn_Login: Button
    private lateinit var service: Facade
    private lateinit var dbLite: DataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        forgotPassword = findViewById(R.id.forgotPassword)
        createAccount = findViewById(R.id.createAccount)
        txtUserEmail = findViewById(R.id.txtUserEmail)
        txtPassword = findViewById(R.id.txtPassword)
        progressBar = findViewById(R.id.progressBar)
        btn_Login = findViewById(R.id.btn_Login)

        dbLite = DataBaseHelper(this)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        service = Facade()

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
                    // Si la autenticacion es satisfactoria obtenemos el token
                    val mUser = FirebaseAuth.getInstance().currentUser
                    mUser!!.getIdToken(true)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val idToken = task.result!!.token
//                                Log.e("idToken", idToken.toString())
                                    // Guardamos el token  para inicio de sesion posterior sin tener que ingresar usuario y contraseña
                                    service.setPreferenceKey(this, "token", idToken)
                                    val docRef = db.collection("usuarios").document(mUser.uid)
                                    docRef.get()
                                            .addOnSuccessListener { document ->
                                                var username = document?.data?.get("nombre").toString() + " " + document?.data?.get("apellido").toString()
                                                var avatar = document?.data?.get("avatar").toString()
                                                service.setPreferenceKey(this, "username", username)
                                                service.setPreferenceKey(this, "avatar", avatar)
                                            }
                                    // Guardamos el idUsuario para realizar consultas a SQLLite
                                    //service.setPreferenceKey(this, "idUsuario", mUser.uid)

                                    //Buscamos el usuario en sQLLite
//                                var test =  dbLite.readData(mUser.uid)
//                                if (test!= null){ // Si el usuario existe
//                                    Log.e(ContentValues.TAG, "Se encontro uruario en SQLLite con idUruario: ${test.email}")
//                                }else { // Si no existe
//                                    Log.e(ContentValues.TAG, "No se encontro uruario en SQLLite")
//                                    getFirebaseCurrentUser(mUser.uid)
//                                }
                                }
                            }
                    startActivity(Intent(this, HomeActivity::class.java))
                } else {
                    progressBar.visibility = View.INVISIBLE
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

    private fun getFirebaseCurrentUser(idUsuario: String?) {
        val docRef = db.collection("usuarios").document(idUsuario!!)
        docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {

                        var myJson = document.data?.get("domicilio").toString()
                        var domicilio = Gson().fromJson(myJson, Domicilio::class.java)

                        var usuario = UserDb()
                        usuario.idUsuario = idUsuario
                        usuario.nombre = document.data?.get("nombre").toString()
                        usuario.apellido = document.data?.get("apellido").toString()
                        usuario.calle = domicilio.calle
                        usuario.numero = domicilio.numero
                        usuario.cp = domicilio.cp
                        usuario.localidad = domicilio.localidad
                        usuario.provincia = domicilio.provincia
                        usuario.pais = domicilio.pais
                        usuario.email = document.data?.get("email").toString()
                        usuario.fechaReg = document.data?.get("fechaReg").toString()
                        usuario.fechaNac = document.data?.get("fechaNac").toString()
                        usuario.tel = document.data?.get("tel").toString()
                        usuario.cel = document.data?.get("cel").toString()

                        dbLite.insertData(usuario)

//                        Log.e(ContentValues.TAG, "DocumentSnapshot data: ${document.data}")
//                        Log.e(ContentValues.TAG, "DocumentSnapshot data nombre: ${document.data?.get("nombre").toString()}")
//                        Log.e(ContentValues.TAG, "DocumentSnapshot data apellido: ${document.data?.get("apellido").toString()}")
//                        Log.e(ContentValues.TAG, "DocumentSnapshot data fechaNac: ${document.data?.get("fechaNac").toString()}")
//                        Log.e(ContentValues.TAG, "DocumentSnapshot data fechaReg: ${document.data?.get("fechaReg").toString()}")
//                        Log.e(ContentValues.TAG, "DocumentSnapshot data email: ${document.data?.get("email").toString()}")


//                        Log.e(ContentValues.TAG, "DocumentSnapshot data calle: ${domicilio.calle}")
//                        Log.e(ContentValues.TAG, "DocumentSnapshot data numero: ${domicilio.numero}")
//                        Log.e(ContentValues.TAG, "DocumentSnapshot data cp: ${domicilio.cp}")
//                        Log.e(ContentValues.TAG, "DocumentSnapshot data ciudad: ${domicilio.ciudad}")
//                        Log.e(ContentValues.TAG, "DocumentSnapshot data provincia: ${domicilio.provincia}")
//                        Log.e(ContentValues.TAG, "DocumentSnapshot data pais: ${domicilio.pais}")

                    } else {
                        Log.e(ContentValues.TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(ContentValues.TAG, "get failed with ", exception)
                }
    }


}

