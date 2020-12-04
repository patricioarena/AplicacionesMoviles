package com.example.jyc

import Models.RegisterDto
import MyResources.Facade
import android.app.DatePickerDialog
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
import java.util.*


/* //Para firebase realtime
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
 */

class RegisterActivity : AppCompatActivity() {

    private lateinit var register: RegisterDto
    private lateinit var service: Facade

    private lateinit var editTextFechaNac: EditText

    private lateinit var name: EditText
    private lateinit var lastname: EditText
    private lateinit var email: EditText
    private lateinit var reemail: EditText
    private lateinit var password: EditText
    private lateinit var repassword: EditText

    private lateinit var progressBar: ProgressBar

    /* //Para firebase realtime
    private  lateinit var dbReference: DatabaseReference
    private  lateinit var database: FirebaseDatabase
    */

    private lateinit var database: FirebaseFirestore

    private lateinit var auth: FirebaseAuth
    private lateinit var btn_register: Button

    private val CERO = "0"
    private val BARRA = "/"

    //Calendario para obtener fecha & hora
    var c = Calendar.getInstance()

    //Variables para obtener la fecha
    var mes = c[Calendar.MONTH]
    var dia = c[Calendar.DAY_OF_MONTH]
    var anio = c[Calendar.YEAR]

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        name = findViewById(R.id.txtName)
        lastname = findViewById(R.id.txtLastName)
        email = findViewById(R.id.txtEmail)
        reemail = findViewById(R.id.txtEmail2)
        password = findViewById(R.id.txtPassword)
        repassword = findViewById(R.id.txtPassword2)

        progressBar = findViewById(R.id.progressBar)
        btn_register = findViewById(R.id.btn_Register)

        auth = FirebaseAuth.getInstance()
        service = Facade()


//        database = FirebaseDatabase.getInstance()//Para firebase realtime
//        dbReference = database.reference.child("User") //Para firebase realtime
//
        database = FirebaseFirestore.getInstance() //Para firebase firestore
//        val users = database.collection("users") //Para firebase firestore

        btn_register.setOnClickListener { createNewAccount() }

        editTextFechaNac = findViewById(R.id.editTextFechaNac)
        editTextFechaNac.setOnClickListener {
            obtenerFecha()
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNewAccount() {


        if (TextUtils.isEmpty(name.text.toString())) {
            Toast.makeText(this, "Name is empty", Toast.LENGTH_SHORT).show()
        }

        if (TextUtils.isEmpty(name.text.toString())) {
            Toast.makeText(this, "Lastname is empty", Toast.LENGTH_SHORT).show()
        }

        if (TextUtils.isEmpty(email.text.toString().toLowerCase())) {
            Toast.makeText(this, "Email is empty", Toast.LENGTH_SHORT).show()
        }

        if (TextUtils.isEmpty(reemail.text.toString().toLowerCase())) {
            Toast.makeText(this, "Email is empty", Toast.LENGTH_SHORT).show()
        }

        if (TextUtils.isEmpty(password.text.toString())) {
            Toast.makeText(this, "Email confirmation is empty", Toast.LENGTH_SHORT).show()
        }

        if (TextUtils.isEmpty(repassword.text.toString())) {
            Toast.makeText(this, "Password confirmation is empty", Toast.LENGTH_SHORT).show()
        } else if (!(email.text.toString().toLowerCase()).equals(
                reemail.text.toString().toLowerCase()
            )
        ) {
            Toast.makeText(this, "Email and email confirmation do not match", Toast.LENGTH_SHORT)
                .show()
        } else if (!(password.text.toString()).equals(repassword.text.toString())) {
            Toast.makeText(
                this,
                "Password and password confirmation do not match",
                Toast.LENGTH_SHORT
            ).show()
        } else {

            register = RegisterDto()
            register.name =
                name.text.toString().toLowerCase().split(" ").joinToString(" ") { it.capitalize() }
            register.lastname = lastname.text.toString().toLowerCase().split(" ")
                .joinToString(" ") { it.capitalize() }
            register.email = email.text.toString().toLowerCase()
            register.password = password.text.toString()
            register.fechaReg = service.getDateTime()
            register.fechaNac = editTextFechaNac.text.toString()
            registerUser(register)

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

    private fun registerUser(model: RegisterDto) {
        // En este bloque se realiza la creacion del usuario en firebase
        auth.createUserWithEmailAndPassword(model.email.toString(), model.password.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    verifyEmail(user)

                    /*// una vez registrado correctamente es almacenada la informacion en usuario en firebase realtime
                    val userDB = dbReference.child(user?.uid.toString())
                    userDB.child("name").setValue(name)
                    userDB.child("lastname").setValue(lastname)
                    */

                    var calle = " la calle loca "
                    var localidad = "florencio varela "
                    var provincia = " buenos aires"
                    var pais = "argentina"

                    localidad = service.removeSpaces(localidad)
                    provincia = service.removeSpaces(provincia)
                    calle = service.removeSpaces(calle)
                    pais = service.removeSpaces(pais)

                    val domicilio = hashMapOf(
                        "calle" to calle,
                        "numero" to 0,
                        "cp" to 0,
                        "ciudad" to localidad,
                        "provincia" to provincia,
                        "pais" to pais,
                        "eCalle_1" to "%20",
                        "eCalle_2" to "%20"
                    )

                    val userInfo = hashMapOf(
                        "nombre" to model.name,
                        "apellido" to model.lastname,
                        "email" to model.email,
                        "fechaReg" to model.fechaReg,
                        "fechaNac" to model.fechaNac,
                        "domicilio" to domicilio,
                        "cel" to "%20",
                        "tel" to "%20",
                        "avatar" to "https://firebasestorage.googleapis.com/v0/b/jyc-appa.appspot.com/o/user-logos-user-logo-png-1920_1280.png?alt=media&token=311b1b6f-b51f-4adb-8a4d-98c7d5aa893d"
                    )

                    val users = database.collection("usuarios")
                    users.document(user?.uid.toString()).set(userInfo)
//                            users.document(user?.uid.toString()).collection("publicaciones")
//                                .add(publicaciones)

                    progressBar.visibility = View.INVISIBLE
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }

    }

    private fun obtenerFecha() {
        val recogerFecha = DatePickerDialog(
            this,
            { view, year, month, dayOfMonth -> //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                val mesActual = month + 1
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                val diaFormateado =
                    if (dayOfMonth < 10) CERO.toString() + dayOfMonth.toString() else dayOfMonth.toString()
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                val mesFormateado =
                    if (mesActual < 10) CERO.toString() + mesActual.toString() else mesActual.toString()
                //Muestro la fecha con el formato deseado
                editTextFechaNac.setText(diaFormateado + BARRA.toString() + mesFormateado + BARRA + year)
            }, //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             * También puede cargar los valores que usted desee
             */
            /**
             * También puede cargar los valores que usted desee
             */
            anio, mes, dia
        )
        //Muestro el widget
        recogerFecha.show()
    }

}




