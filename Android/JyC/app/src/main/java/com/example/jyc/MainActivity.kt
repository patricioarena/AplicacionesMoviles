package com.example.jyc

import MyResources.Storage
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var pressedTime: Long = 0
    private lateinit var btn_Logout: Button
    private lateinit var service: Storage

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_Logout = findViewById(R.id.btn_Logout)
        service = Storage()

        btn_Logout.setOnClickListener {
            logoutUser()
        }
    }

    private fun logoutUser() {
        service.deletePreferenceKey(this,"user")
        service.deletePreferenceKey(this,"password")
        super.finishAffinity()
    }

    override fun onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.finishAffinity()
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }

        pressedTime = System.currentTimeMillis();
    }
}