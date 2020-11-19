package com.example.jyc

import MyResources.Storage
import android.content.Intent

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity


class HomeActivity : AppCompatActivity() {
    private var pressedTime: Long = 0
    private lateinit var service: Storage
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        service = Storage()

        // Agregar toolbar personalizado a activity main
        toolbar = findViewById(R.id.myToolbar)
        toolbar.title = "HomeActivity";
        toolbar.subtitle = "Contenido principal del feed";
        setSupportActionBar(toolbar)

    }


    // Si la autenticacion es satisfactoria guardamos el token
    // por lo tanto la implementacion de la funcionalidad de logout
    // se basa en el paso inverso que es borrar el token y posteriormente cerrar la aplicacion
    // tambien podriamos reenviar al usuario al login
    private fun logoutUser() {
        service.deletePreferenceKey(this, "token")
        super.finishAffinity()
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