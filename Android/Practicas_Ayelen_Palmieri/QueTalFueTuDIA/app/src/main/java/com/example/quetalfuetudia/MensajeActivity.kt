package com.example.quetalfuetudia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text

class MensajeActivity : AppCompatActivity() {

    private lateinit var txt_mensaje : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mensaje)

        var opcion = intent.getStringExtra("opcion")

        txt_mensaje = findViewById(R.id.txt_mensaje)

        actualizarMensaje(opcion)
//        Toast.makeText(this@MensajeActivity, opcion, Toast.LENGTH_LONG).show()
    }

    private fun actualizarMensaje(tKey: String?) {
        when (tKey) {
            "0" -> {
                val msg: String = getString(R.string.mensajes1)
                txt_mensaje!!.text = msg
            }
            "1" -> {
                val msg: String = getString(R.string.mensajes2)
                txt_mensaje!!.text = msg
            }
            "2" -> {
                val msg: String = getString(R.string.mensajes3)
                txt_mensaje!!.text = msg
            }
            "3" -> {
                val msg: String = getString(R.string.mensajes4)
                txt_mensaje!!.text = msg
            }
        }}
}