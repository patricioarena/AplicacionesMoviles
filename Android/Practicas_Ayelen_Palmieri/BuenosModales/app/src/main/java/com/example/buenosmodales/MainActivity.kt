package com.example.buenosmodales

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var btn_gracias: Button
    private lateinit var btn_estornudar : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_gracias = findViewById(R.id.btn_gracias)
        btn_estornudar = findViewById(R.id.btn_estornudar)

        btn_gracias.setOnClickListener(graciasListener)
        btn_estornudar.setOnClickListener(estornudarListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
//        getMenuInflater().inflate(R.menu.main, menu)
        return true
    }

    private val graciasListener = object : View.OnClickListener {

        override fun onClick(view: View?) {
            Toast.makeText(this@MainActivity, "De nada!", Toast.LENGTH_SHORT).show()
        }
    }

    private val estornudarListener = object : View.OnClickListener {

        override fun onClick(v: View?) {
            Toast.makeText(this@MainActivity, "Salud!", Toast.LENGTH_SHORT).show()
        }
    }
}