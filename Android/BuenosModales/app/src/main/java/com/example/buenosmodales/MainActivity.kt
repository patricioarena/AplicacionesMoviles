package com.example.buenosmodales

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private var btn_gracias: Button? = null
    private var btn_estornudar: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_BuenosModales);
        setContentView(R.layout.activity_main)

        // Asocio las instancias a la interfaz
        btn_gracias = findViewById<View>(R.id.btn_gracias) as Button
        btn_estornudar = findViewById<View>(R.id.btn_estornudar) as Button

        // Registro un listener para cada botón
        btn_gracias!!.setOnClickListener {
            // Muestro el mensaje "De nada"
            Toast.makeText(this@MainActivity, "De nada!",
                    Toast.LENGTH_LONG).show()
        }

        btn_estornudar!!.setOnClickListener {
            // Muestro el mensaje "Salud!“
            Toast.makeText(this@MainActivity, "Salud!",
                    Toast.LENGTH_LONG).show()
        }
    }







}