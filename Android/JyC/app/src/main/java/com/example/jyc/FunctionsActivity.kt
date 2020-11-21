package com.example.jyc

import MyResources.Facade
import android.Manifest
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.util.*


class FunctionsActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var service: Facade
    private lateinit var textView6: TextView

    private lateinit var button_1: Button
    private lateinit var button_2: Button
    private lateinit var auth: FirebaseAuth

    private var mStorageRef: StorageReference? = null
    private var imageView: ImageView? = null
    private var bitmap: Bitmap? = null
    var currentPhotoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_functions)

        service = Facade()

        // Agregar toolbar personalizado a activity main
        toolbar = findViewById(R.id.myToolbar)
        toolbar.title = "FuntionActivity";
        toolbar.subtitle = "Test functions DEV";

        setSupportActionBar(toolbar)
        auth = FirebaseAuth.getInstance()
        mStorageRef = FirebaseStorage.getInstance().getReference();

        button_1 = findViewById(R.id.button_1)
        button_2 = findViewById(R.id.button_2)

        textView6 = findViewById(R.id.textView6)
        imageView = findViewById(R.id.image)

        button_1.isEnabled = false
        button_1.text = "Take pic"

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
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
                startActivity(Intent(this, FunctionsActivity::class.java))
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
        val stream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val random: String = UUID.randomUUID().toString()
        val imageRef: StorageReference? = mStorageRef?.child("image/$random")

        val b: ByteArray = stream.toByteArray()
        if (imageRef != null) {
            imageRef.putBytes(b)
                .addOnSuccessListener { taskSnapshot ->
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                        val downloadUri = uri
                        textView6.text = downloadUri.toString()
                    }
                    Toast.makeText(this, "Photo Uploaded", Toast.LENGTH_SHORT).show();
                }
                .addOnFailureListener {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    Toast.makeText(this, "Upload Failed", Toast.LENGTH_SHORT).show();
                }
        }
    }




}

