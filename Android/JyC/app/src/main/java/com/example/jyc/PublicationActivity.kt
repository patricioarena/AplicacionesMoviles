package com.example.jyc

import MyResources.Facade
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
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
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.util.*


class PublicationActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var service: Facade
    private lateinit var textView6: TextView
    private lateinit var editTextTextMultiLine: EditText
    private lateinit var button_1: Button
    private lateinit var button_2: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private var mStorageRef: StorageReference? = null
    private var imageView: ImageView? = null
    private var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publication)

        service = Facade()

        // Agregar toolbar personalizado a activity main
        toolbar = findViewById(R.id.myToolbar)
        toolbar.title = "Nueva publication";
        toolbar.subtitle = "My super publication"

        setSupportActionBar(toolbar)

        auth = FirebaseAuth.getInstance()
        mStorageRef = FirebaseStorage.getInstance().getReference();
        database = FirebaseFirestore.getInstance()

        button_1 = findViewById(R.id.button_1)
        button_2 = findViewById(R.id.button_2)

        textView6 = findViewById(R.id.textView6)
        editTextTextMultiLine = findViewById(R.id.editTextTextMultiLine)

        imageView = findViewById(R.id.image)

        button_1.isEnabled = false
        button_1.text = "Take pic"

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 111)
        } else {
            button_1.isEnabled = true
            button_1.setOnClickListener {
                var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(i, 101)
            }

        }

        button_2.text = "Upload"
        button_2.setOnClickListener {
            upload()
        }

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
            R.id.nav_gallery -> {
                Toast.makeText(this, "Mover a activity Gallery", Toast.LENGTH_SHORT).show();
                return true
            }
            R.id.nav_testActivity -> {
                startActivity(Intent(this, MyFunActivity::class.java))
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

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            button_1.isEnabled = true
        }
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
                            textView6.text = imagenUri

                            var emptyStringArray = listOf("")
                            var categorias = emptyStringArray
                            var tags = emptyStringArray
                            var comentarios = emptyStringArray
                            var likes = emptyStringArray
                            var fecha = service.getDateTime()
                            var textomuylargo = editTextTextMultiLine.text.toString()

                            var publicacion = hashMapOf(
                                    "idUsuario" to idUsuario,
                                    "articulo" to textomuylargo,
                                    "fecha" to fecha,
                                    "imagen" to imagenUri,
                                    "categorias" to categorias,
                                    "tags" to tags,
                                    "comentarios" to comentarios,
                                    "likes" to likes
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

