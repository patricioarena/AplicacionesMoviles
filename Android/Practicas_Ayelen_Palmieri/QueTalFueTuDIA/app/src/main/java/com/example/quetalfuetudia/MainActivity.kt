package com.example.quetalfuetudia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var btn_enviar : Button
    private lateinit var rg_respuestas : RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_enviar = findViewById(R.id.btn_enviar)
        rg_respuestas = findViewById(R.id.rg_respuestas)

        btn_enviar!!.setOnClickListener(enviarListener)

//        radioGroup.setOnCheckedChangeListener { group, checkedId ->
//            if(checkedId == R.id.radioButton1)
//                Toast.makeTe
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    private val enviarListener = object : View.OnClickListener {

        override fun onClick(view: View?){
            val radioButtonId: Int = rg_respuestas.checkedRadioButtonId
            val radioButton: View = rg_respuestas.findViewById(radioButtonId)
            val idx: Int = rg_respuestas.indexOfChild(radioButton)


            val intent:Intent = Intent(this@MainActivity, MensajeActivity::class.java)
            val bundle:Bundle = Bundle()
            bundle.putString("opcion", idx.toString())
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }
}

