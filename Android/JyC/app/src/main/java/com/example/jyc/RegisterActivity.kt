package com.example.jyc

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime


/* //Para firebase realtime
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
 */

class RegisterActivity : AppCompatActivity() {

    private lateinit var txtName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtPassword: EditText
    private lateinit var txtPassword2: EditText
    private lateinit var progressBar: ProgressBar

    /* //Para firebase realtime
    private  lateinit var dbReference: DatabaseReference
    private  lateinit var database: FirebaseDatabase
    */

    private lateinit var database: FirebaseFirestore

    private lateinit var auth: FirebaseAuth
    private lateinit var btn_register: Button

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        txtName = findViewById(R.id.txtName)
        txtLastName = findViewById(R.id.txtLastName)
        txtEmail = findViewById(R.id.txtEmail)
        txtPassword = findViewById(R.id.txtPassword)
        txtPassword2 = findViewById(R.id.txtPassword2)
        progressBar = findViewById(R.id.progressBar)
        btn_register = findViewById(R.id.btn_Register)

        auth = FirebaseAuth.getInstance()

//        database = FirebaseDatabase.getInstance()//Para firebase realtime
//        dbReference = database.reference.child("User") //Para firebase realtime
//
        database = FirebaseFirestore.getInstance() //Para firebase firestore
//        val users = database.collection("users") //Para firebase firestore

        btn_register.setOnClickListener { createNewAccount() }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNewAccount() {
        val name: String = txtName.text.toString()
        val lastname: String = txtLastName.text.toString()
        val email: String = txtEmail.text.toString()
        val password: String = txtPassword.text.toString()
        val password2: String = txtPassword2.text.toString()


        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(lastname) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(
                password
            )
        ) {

            if (password.equals(password2)) {
                progressBar.visibility = View.VISIBLE

                // En este bloque se realiza la creacion del usuario en firebase
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user: FirebaseUser? = auth.currentUser
                            verifyEmail(user)

                            /*// una vez registrado correctamente es almacenada la informacion en usuario en firebase realtime
                            val userDB = dbReference.child(user?.uid.toString())
                            userDB.child("name").setValue(name)
                            userDB.child("lastname").setValue(lastname)
                            */

                            val publicaciones = listOf(
                                "ObjectId(507f191e810c19729de860e3)",
                                "ObjectId(507f191e810c19729de860e3)",
                                "ObjectId(507f191e810c19729de860e3)"
                            )

                            val domicilio = hashMapOf(
                                "calle" to 626,
                                "numero" to 452,
                                "cp" to 1888,
                                "ciudad" to "florencio varela",
                                "prov_est" to "buenos aires",
                                "pais" to "argentina"
                            )

                            val telephones = hashMapOf(
                                "cel" to 1133142754,
                                "tel" to 42740810
                            )

//                            var date = LocalDateTime.now().dayOfMonth.toString() +"/"+ LocalDateTime.now().monthValue.toString() +"/"+ LocalDateTime.now().year.toString()

                            val userInfo = hashMapOf(
                                "nombre" to name,
                                "apellido" to lastname,
                                "email" to email,
                                "fechaReg" to "date",
                                "domicilio" to domicilio,
                                "telefonos" to telephones,
                                "publicaciones" to publicaciones
                                )

                            val users = database.collection("usuarios")
                            users.document(user?.uid.toString()).set(userInfo)
//                            users.document(user?.uid.toString()).collection("publicaciones")
//                                .add(publicaciones)

                            progressBar.visibility = View.INVISIBLE
                            startActivity(Intent(this, LoginActivity::class.java))
                        }
                    }
            } else {
                Toast.makeText(
                    this,
                    "La contraseña y la confirmacion no coinciden",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun verifyEmail(user: FirebaseUser?) {
        user?.sendEmailVerification()?.addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Email enviado", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Error al enviar email", Toast.LENGTH_LONG).show()
            }
        }
    }
}