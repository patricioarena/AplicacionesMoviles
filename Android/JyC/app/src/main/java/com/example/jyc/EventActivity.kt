package com.example.jyc

import MyResources.Facade
import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Layout
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*


class EventActivity : AppCompatActivity(){

    private lateinit var toolbar: Toolbar
    private lateinit var service: Facade
    private lateinit var editTextDate: EditText
    private lateinit var editTextTime: EditText
    private lateinit var editTextTextUrl: EditText

    private lateinit var imageViewBanner: ImageView
    private lateinit var radioButtonUrl: RadioButton
    private lateinit var radioButtonEnPersona: RadioButton
    private lateinit var linearLayout: ConstraintLayout


    private val CERO = "0"
    private val BARRA = "/"
    private val DOS_PUNTOS = ":"

    //Calendario para obtener fecha & hora
    var c = Calendar.getInstance()

    //Variables para obtener la fecha
    var mes = c[Calendar.MONTH]
    var dia = c[Calendar.DAY_OF_MONTH]
    var anio = c[Calendar.YEAR]

    //Variables para obtener la hora hora
    var hora = c[Calendar.HOUR_OF_DAY]
    var minuto = c[Calendar.MINUTE]

    //image pick code
    private val IMAGE_PICK_CODE = 1000;

    //Permission code
    private val PERMISSION_CODE = 1001;

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        service = Facade()

        // Agregar toolbar personalizado a activity main
        toolbar = findViewById(R.id.myToolbar)
        toolbar.title = "Jardines y Cultivos"
        toolbar.subtitle = "Crear nuevo evento";

        setSupportActionBar(toolbar)

        editTextDate = findViewById(R.id.editTextDate)
        editTextDate.setOnClickListener {
            obtenerFecha()
        }

        editTextTime = findViewById(R.id.editTextTime)
        editTextTime.setOnClickListener {
            obtenerHora()
        }

        imageViewBanner = findViewById(R.id.imageViewBanner)
        imageViewBanner.setOnClickListener {
            //check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_DENIED
                ) {
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                } else {
                    //permission already granted
                    pickImageFromGallery();
                }
            } else {
                //system OS is < Marshmallow
                pickImageFromGallery();
            }
        }

        editTextTextUrl = findViewById(R.id.editTextTextUrl)
        linearLayout = findViewById(R.id.linearLayout)

        radioButtonUrl = findViewById(R.id.radioButtonUrl)
        radioButtonUrl.setOnClickListener {
            editTextTextUrl.setVisibility(View.VISIBLE)
            linearLayout.setVisibility(View.GONE)
        }

        radioButtonEnPersona = findViewById(R.id.radioButtonEnPersona)
        radioButtonEnPersona.setOnClickListener {
            linearLayout.setVisibility(View.VISIBLE)
            editTextTextUrl.setVisibility(View.GONE);
            editTextTextUrl.text.clear()
        }

//        findViewById<View>(R.id.btn1_add).setOnTouchListener { v, event ->
//            if (event.action == MotionEvent.ACTION_DOWN) {
//                // start tiemr
//            } else if (event.action == MotionEvent.ACTION_UP) {
//                // stop timer.
//            }
//            false
//        }
    }


    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imageViewBanner.setImageURI(data?.data)
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
                    editTextDate.setText(diaFormateado + BARRA.toString() + mesFormateado + BARRA + year)
                }, //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
                /**
                 * También puede cargar los valores que usted desee
                 */
                anio, mes, dia
        )
        //Muestro el widget
        recogerFecha.show()
    }

    private fun obtenerHora() {
        val recogerHora = TimePickerDialog(
                this,
                { view, hourOfDay, minute -> //Formateo el hora obtenido: antepone el 0 si son menores de 10
                    val horaFormateada = if (hourOfDay < 10) CERO + hourOfDay else hourOfDay.toString()
                    //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                    val minutoFormateado = if (minute < 10) CERO + minute else minute.toString()
                    //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                    val AM_PM: String
                    AM_PM = if (hourOfDay < 12) {
                        "a.m."
                    } else {
                        "p.m."
                    }
                    //Muestro la hora con el formato deseado
                    editTextTime.setText(horaFormateada + DOS_PUNTOS.toString() + minutoFormateado + " " + AM_PM)
                }, //Estos valores deben ir en ese orden
                //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
                //Pero el sistema devuelve la hora en formato 24 horas
                hora, minuto, false
        )
        recogerHora.show()
    }

    private fun logoutUser() {
        service.deletePreferenceKey(this, "token")
        super.finishAffinity()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_toobar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.menu_settings -> {
                Toast.makeText(this, "Mover a activity Settings", Toast.LENGTH_SHORT).show();
                return true
            }
            R.id.nav_home -> {
                startActivity(Intent(this, HomeActivity::class.java))
                return true
            }
            R.id.nav_testActivity -> {
                startActivity(Intent(this, FunctionsActivity::class.java))
                return true
            }
            R.id.nav_new_event -> {
                startActivity(Intent(this, EventActivity::class.java))
                return true
            }
            R.id.nav_new_pub -> {
                startActivity(Intent(this, PublicationActivity::class.java))
                return true
            }
            R.id.nav_logout -> {
                logoutUser()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

