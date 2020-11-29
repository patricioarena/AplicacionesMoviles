package com.example.quetalfuetudia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class MainActivity2 : AppCompatActivity() {
    private var rg_text: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        rg_text = findViewById<View>(R.id.rg_text) as TextView

        val objectIntent: Intent = intent
        var TKey = objectIntent.getStringExtra("TKey")

        this.updateMsg(TKey);

    }

    private fun updateMsg(tKey: String?) {
        when (tKey) {
            "excelente" -> {
                val msg: String = getString(R.string.app_msg1)
                rg_text!!.text = msg
            }
            "bueno" -> {
                val msg: String = getString(R.string.app_msg2)
                rg_text!!.text = msg
            }
            "malo" -> {
                val msg: String = getString(R.string.app_msg3)
                rg_text!!.text = msg
            }
            "pesimo" -> {
                val msg: String = getString(R.string.app_msg4)
                rg_text!!.text = msg
            }
        }
    }
}