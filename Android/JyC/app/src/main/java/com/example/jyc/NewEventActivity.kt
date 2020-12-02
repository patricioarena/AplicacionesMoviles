package com.example.jyc

import MyResources.Facade
import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.gms.maps.GoogleMap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.*


class NewEventActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var service: Facade
    private lateinit var editTextDate: EditText
    private lateinit var editTextTime: EditText
    private lateinit var editTextTextNombreEvento: EditText

    private lateinit var editTextProvincia: EditText
    private lateinit var editTextLocalidad: EditText
    private lateinit var editTextCp: EditText
    private lateinit var editTextCalle: EditText
    private lateinit var editTextNumero: EditText
    private lateinit var editTextPiso: EditText
    private lateinit var editTextEntreCalle1: EditText
    private lateinit var editTextEntreCalle2: EditText

    private lateinit var editTextTextUrl: EditText
    private lateinit var editTextTextMultiLineEvento: EditText
    private lateinit var imageViewBanner: ImageView
    private lateinit var radioButtonUrl: RadioButton
    private lateinit var radioButtonEnPersona: RadioButton
    private lateinit var linearLayout: ConstraintLayout
    private lateinit var buttonDescartarEvento: Button
    private lateinit var buttonPublicarEvento: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private var mStorageRef: StorageReference? = null
    private var bitmap: Bitmap? = null
    private val CERO = "0"
    private val BARRA = "/"
    private val DOS_PUNTOS = ":"
    private var typeEvent: String? = null

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
        setContentView(R.layout.activity_new_event)

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

        editTextTextNombreEvento = findViewById(R.id.editTextTextNombreEvento)
        editTextTextUrl = findViewById(R.id.editTextTextUrl)
        linearLayout = findViewById(R.id.linearLayout)

        radioButtonUrl = findViewById(R.id.radioButtonUrl)
        radioButtonUrl.setOnClickListener {
            editTextTextUrl.setVisibility(View.VISIBLE)
            linearLayout.setVisibility(View.GONE)
            typeEvent = Evento.ONLINE.toString()
        }

        radioButtonEnPersona = findViewById(R.id.radioButtonEnPersona)
        radioButtonEnPersona.setOnClickListener {
            linearLayout.setVisibility(View.VISIBLE)
            editTextTextUrl.setVisibility(View.GONE);
            editTextTextUrl.text.clear()
            typeEvent = Evento.PRESENCIAL.toString()
        }

        buttonDescartarEvento = findViewById(R.id.buttonDescartarEvento)
        buttonDescartarEvento.setOnClickListener {
            typeEvent = null;
            startActivity(Intent(this, HomeActivity::class.java))
        }

        buttonPublicarEvento = findViewById(R.id.buttonPublicarEvento)
        buttonPublicarEvento.setOnClickListener {

            uploadEvent()

        }

        editTextProvincia = findViewById(R.id.editTextProvincia)
        editTextLocalidad = findViewById(R.id.editTextLocalidad)
        editTextCp = findViewById(R.id.editTextCp)
        editTextCalle = findViewById(R.id.editTextCalle)
        editTextNumero = findViewById(R.id.editTextNumero)
        editTextPiso = findViewById(R.id.editTextPiso)
        editTextEntreCalle1 = findViewById(R.id.editTextEntreCalle1)
        editTextEntreCalle2 = findViewById(R.id.editTextEntreCalle2)
        editTextTextMultiLineEvento = findViewById(R.id.editTextTextMultiLineEvento)

        auth = FirebaseAuth.getInstance()
        mStorageRef = FirebaseStorage.getInstance().getReference();
        database = FirebaseFirestore.getInstance()
    }


    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK && data != null) {

            val selectedPhotoUri = data.data
            try {
                selectedPhotoUri?.let {
                    if (Build.VERSION.SDK_INT < 28) {
                        val bitmapx = MediaStore.Images.Media.getBitmap(
                            this.contentResolver,
                            selectedPhotoUri
                        )
                        imageViewBanner.setImageBitmap(bitmapx)
                    } else {
                        val source = ImageDecoder.createSource(this.contentResolver, selectedPhotoUri)
                        val bitmapx = ImageDecoder.decodeBitmap(source)
                        bitmap = bitmapx
                        imageViewBanner.setImageBitmap(bitmapx)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
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
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_toobar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
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
                startActivity(Intent(this, MyFunActivity::class.java))
                return true
            }
            R.id.nav_new_event -> {
                startActivity(Intent(this, NewEventActivity::class.java))
                return true
            }
            R.id.nav_new_pub -> {
                startActivity(Intent(this, PublicationActivity::class.java))
                return true
            }
            R.id.nav_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                return true
            }
            R.id.nav_logout -> {
                logoutUser()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun uploadEvent() {

        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )

        val user: FirebaseUser? = auth.currentUser
        var idUsuario = user?.uid.toString()
        var username = service.getPreferenceKey(this, "username")
        var avatar = service.getPreferenceKey(this, "avatar")

        val stream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val random: String = UUID.randomUUID().toString()
        val imageRef: StorageReference? = mStorageRef?.child("$idUsuario/$random")

        val b: ByteArray = stream.toByteArray()
        if (imageRef != null) {
            imageRef.putBytes(b)
                .addOnSuccessListener { taskSnapshot ->
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                        val downloadUri = uri
                        var imagenUri = downloadUri.toString()

                        var emptyStringArray = listOf("")
                        var tags = emptyStringArray
                        var fecha = service.getDateTime()
                        var textomuylargo = editTextTextMultiLineEvento.text.toString()
                        var nombreEvento = editTextTextNombreEvento.text.toString()
                        var fechaEvento = editTextDate.text.toString()
                        var hora = editTextTime.text.toString()

                        if (typeEvent.equals(Evento.PRESENCIAL.toString())) {

                            var calle = editTextCalle.text.toString()
                            var eCalle1 = editTextEntreCalle1.text.toString()
                            var eCalle2 = editTextEntreCalle2.text.toString()
                            var numero = editTextNumero.text.toString()
                            var cp = editTextCp.text.toString()
                            var piso = editTextPiso.text.toString()
                            var localidad = editTextLocalidad.text.toString()
                            var provincia = editTextProvincia.text.toString()

                            var loc = service.removeSpaces(localidad)
                            var prov = service.removeSpaces(provincia)
                            var c = service.removeSpaces(calle)
                            var c1 = service.removeSpaces(eCalle1)
                            var c2 = service.removeSpaces(eCalle2)
                            var p = service.removeSpaces(piso)

                            var eP = listOf("Evento PRESENCIAL")

                            val lugar = hashMapOf(
                                "eCalle_1" to c1,
                                "eCalle_2" to c2,
                                "calle" to c,
                                "numero" to numero,
                                "cp" to cp,
                                "piso" to p,
                                "localidad" to loc,
                                "provincia" to prov,
                                "pais" to "Argentina"
                            )

                            var eventoPresencial = hashMapOf(
                                "idUsuario" to idUsuario,
                                "nombreEvento" to nombreEvento,
                                "articulo" to textomuylargo,
                                "fecha" to fecha,
                                "imagen" to imagenUri,
                                "fechaEvento" to fechaEvento,
                                "hora" to hora,
                                "categorias" to eP,
                                "Tipo" to Evento.PRESENCIAL.toString(),
                                "tags" to tags,
                                "lugar" to lugar
                            )

                            val publicacionesDb = database.collection("publicaciones")
                            publicacionesDb.add(eventoPresencial).addOnSuccessListener { documentReference ->
                                database.collection("usuarios").document(idUsuario)
                                    .update("publicaciones", FieldValue.arrayUnion(documentReference.id))
                                    .addOnSuccessListener {
                                        var jsonObject = JSONObject (
                                            """{
                                                    "to": "/topics/PRESENCIAL",
                                                    "notification": {
                                                        "title": "Nuevo evento PRESENCIAL",
                                                        "body": "${username} a publicado un nuevo evento",
                                                        "image": "${downloadUri}",
                                                        "sound": "default"
                                                     },
                                                     "data": {
                                                        "title": "Nuevo evento PRESENCIAL.. data",
                                                        "body": "${username} a publicado un nuevo evento.. data",
                                                        "idPublicacion": "${documentReference.id}"
                                                      }
                                                    }"""
                                        )
                                        getRemoteConfigAndSendNotificationCloudMessaging(jsonObject)
                                        Toast.makeText(this, "Event Uploaded", Toast.LENGTH_SHORT).show();

                                    }
                                    .addOnCompleteListener {
                                        startActivity(Intent(this, HomeActivity::class.java))
                                    }
                            }

                        } else if (typeEvent.equals(Evento.ONLINE.toString())) {

                            var eO = listOf("Evento ONLINE")

                            var eventoOnline = hashMapOf(
                                "idUsuario" to idUsuario,
                                "nombreEvento" to nombreEvento,
                                "articulo" to textomuylargo,
                                "fecha" to fecha,
                                "imagen" to imagenUri,
                                "fechaEvento" to fechaEvento,
                                "hora" to hora,
                                "categorias" to eO,
                                "tipo" to Evento.ONLINE.toString(),
                                "url" to editTextTextUrl.text.toString(),
                                "tags" to tags
                            )

                            val publicacionesDb = database.collection("publicaciones")
                            publicacionesDb.add(eventoOnline).addOnSuccessListener { documentReference ->
                                database.collection("usuarios").document(idUsuario)
                                    .update("publicaciones", FieldValue.arrayUnion(documentReference.id))
                                    .addOnSuccessListener {
                                        var jsonObject = JSONObject (
                                                """{
                                                    "to": "/topics/ONLINE",
                                                    "notification": {
                                                        "title": "Nuevo evento ONLINE",
                                                        "body": "${username} a publicado un nuevo evento",
                                                        "image": "${downloadUri}",
                                                        "sound": "default"
                                                     },
                                                     "data": {
                                                        "title": "Nuevo evento ONLINE.. data",
                                                        "body": "${username} a publicado un nuevo evento.. data",
                                                        "idPublicacion": "${documentReference.id}"
                                                      }
                                                    }""""
                                                )
                                        getRemoteConfigAndSendNotificationCloudMessaging(jsonObject)
                                        Toast.makeText(this, "Event Uploaded", Toast.LENGTH_SHORT).show();

                                    }
                                    .addOnCompleteListener {
                                        startActivity(Intent(this, HomeActivity::class.java))
                                    }
                            }

                        } else {
                            Toast.makeText(this, "Event Upload Failed", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
                .addOnFailureListener {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    Toast.makeText(this, "Upload Failed", Toast.LENGTH_SHORT).show();
                }
        }
    }


    enum class Evento {
        ONLINE, PRESENCIAL
    }

    class CustomJsonObjectRequestBasicAuth(
        method: Int, url: String,
        jsonObject: JSONObject?,
        listener: Response.Listener<JSONObject>,
        errorListener: Response.ErrorListener,
        credentials: String
    ) : JsonObjectRequest(method, url, jsonObject, listener, errorListener) {

        private var mCredentials: String = credentials

        @Throws(AuthFailureError::class)
        override fun getHeaders(): Map<String, String> {
            val auth = mCredentials
            val headers = HashMap<String, String>()
            headers["Content-Type"] = "application/json"
            headers["Authorization"] = auth
            return headers
        }
    }


    fun sendNotificationCloudMessaging(url: String, jsonObject: JSONObject?, credentials: String) {
        // Make a volley custom json object request with basic authentication
        val request = CustomJsonObjectRequestBasicAuth(
            Request.Method.POST, url, jsonObject,
            { response ->
                try {
                    Log.e("Response", "$response")
                    Toast.makeText(this, "Notificacion enviada response: ${response}", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    Log.e("Error", e.printStackTrace().toString())
                }
            }, {
                Log.e("Error", " $it")
            }, credentials
        )
        // Add the volley request to request queue
        VolleySingleton.getInstance(this).addToRequestQueue(request)
    }


    fun getRemoteConfigAndSendNotificationCloudMessaging(jsonObject: JSONObject?): String? {
        var value: String? = null
        var remoteConfig = FirebaseRemoteConfig.getInstance()
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    var credentials = remoteConfig.getString("Authorization")
//                    Log.d("TAG", "Credentials: $credentials")
//                    Toast.makeText(this, "Fetch and activate succeeded", Toast.LENGTH_SHORT).show
                    sendNotificationCloudMessaging("https://fcm.googleapis.com/fcm/send", jsonObject, credentials)

                } else {
                    Log.e("Error", task.exception.toString())
                }

            }
        return value
    }

}

