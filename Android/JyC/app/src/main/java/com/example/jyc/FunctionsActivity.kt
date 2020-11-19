package com.example.jyc

import MyResources.Storage
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.auth0.android.jwt.JWT
import com.google.firebase.auth.FirebaseAuth
import java.util.*


class FunctionsActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var service: Storage
    private lateinit var textView6: TextView

    private lateinit var button_1: Button
    private lateinit var button_2: Button
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_functions)

        service = Storage()

        // Agregar toolbar personalizado a activity main
        toolbar = findViewById(R.id.myToolbar)
        toolbar.title = "FuntionActivity";
        toolbar.subtitle = "Test functions DEV";

        setSupportActionBar(toolbar)
        auth = FirebaseAuth.getInstance()

        button_1 = findViewById(R.id.button_1)
        button_2 = findViewById(R.id.button_2)

        textView6 = findViewById(R.id.textView6)

        button_1.setOnClickListener {


        }

        button_2.setOnClickListener {


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

}

