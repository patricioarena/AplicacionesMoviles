package com.example.jyc

import MyResources.Facade
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import android.widget.ImageView
import java.util.*


class PublicationActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var service: Facade
    private lateinit var editTextTextMultiLine: EditText
    private lateinit var buttonDescartar: Button
    private lateinit var buttonPublicar: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private var mStorageRef: StorageReference? = null
    private lateinit var imageView: ImageView
    private var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publication)

        service = Facade()

        // Agregar toolbar personalizado a activity main
        toolbar = findViewById(R.id.myToolbar)
        toolbar.title = "Jardines y Cultivos";
        toolbar.subtitle = "Nueva publicación"

        setSupportActionBar(toolbar)

        auth = FirebaseAuth.getInstance()
        mStorageRef = FirebaseStorage.getInstance().getReference();
        database = FirebaseFirestore.getInstance()

        buttonDescartar = findViewById(R.id.buttonDescartar)
        buttonDescartar.setOnClickListener{
            startActivity(Intent(this, HomeActivity::class.java))
        }

        editTextTextMultiLine = findViewById(R.id.editTextTextMultiLine)

        imageView = findViewById(R.id.image)
        imageView.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 111)
            } else {
                takepick();
            }

        }

        buttonPublicar = findViewById(R.id.buttonPublicar)
        buttonPublicar.setOnClickListener {
            upload()
        }

    }

    fun takepick() {
        var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(i, 101)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            bitmap = data?.getParcelableExtra("data")
            imageView?.setImageBitmap(bitmap)
        }
    }

    private fun upload() {

        window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )

        val user: FirebaseUser? = auth.currentUser
        var idUsuario = user?.uid.toString()

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
                            var categorias = emptyStringArray
                            var tags = emptyStringArray
                            var comentarios = emptyStringArray
                            var fecha = service.getDateTime()
                            var textomuylargo = editTextTextMultiLine.text.toString()

                            var publicacion = hashMapOf(
                                    "idUsuario" to idUsuario,
                                    "articulo" to textomuylargo,
                                    "fecha" to fecha,
                                    "imagen" to imagenUri,
                                    "categorias" to categorias,
                                    "tags" to tags,
                                    "comentarios" to comentarios
                            )

                            val publicacionesDb = database.collection("publicaciones")
                            publicacionesDb.add(publicacion).addOnSuccessListener { documentReference ->
                                database.collection("usuarios").document(idUsuario)
                                        .update("publicaciones", FieldValue.arrayUnion(documentReference.id))
                                        .addOnSuccessListener {
                                            Toast.makeText(this, "Photo Uploaded", Toast.LENGTH_SHORT).show();
                                        }
                            }
                        }

                    }
                    .addOnFailureListener {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        Toast.makeText(this, "Upload Failed", Toast.LENGTH_SHORT).show();
                    }
        }
    }


}

