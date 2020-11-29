package com.example.quetalfuetudia

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private var btn_enviar: Button? = null;
    private var rg_respuestas: RadioGroup? = null
    private var radioButton1: RadioButton? = null
    private var radioButton2: RadioButton? = null
    private var radioButton3: RadioButton? = null
    private var radioButton4: RadioButton? = null
    private var aux: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_enviar = findViewById<View>(R.id.btn_enviar) as Button
        rg_respuestas = findViewById<View>(R.id.rg_respuestas) as RadioGroup
        radioButton1 = findViewById<View>(R.id.radioButton1) as RadioButton
        radioButton2 = findViewById<View>(R.id.radioButton2) as RadioButton
        radioButton3 = findViewById<View>(R.id.radioButton3) as RadioButton
        radioButton4 = findViewById<View>(R.id.radioButton4) as RadioButton

        btn_enviar!!.setOnClickListener(enviarListener)
        rg_respuestas!!.setOnCheckedChangeListener(changeListener)

    }


    private val enviarListener =
        View.OnClickListener {
            var TValue = aux

            val intent: Intent = Intent(this, MainActivity2::class.java)
            intent.putExtra("TKey", TValue)
            startActivity(intent)

        }

    private val changeListener =
        RadioGroup.OnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.radioButton1) {
                aux = radioButton1!!.text.toString().toLowerCase()
                Toast.makeText(this, aux, Toast.LENGTH_SHORT).show()
            }
            if (checkedId == R.id.radioButton2) {
                aux = radioButton2!!.text.toString().toLowerCase()
                Toast.makeText(this, aux, Toast.LENGTH_SHORT).show()
            }
            if (checkedId == R.id.radioButton3) {
                aux = radioButton3!!.text.toString().toLowerCase()
                Toast.makeText(this, aux, Toast.LENGTH_SHORT).show()
            }
            if (checkedId == R.id.radioButton4) {
                aux = radioButton4!!.text.toString().toLowerCase()
                Toast.makeText(this, aux, Toast.LENGTH_SHORT).show()
            }
        }
}
