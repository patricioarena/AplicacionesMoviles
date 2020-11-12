package com.example.jyc

import MyResources.Storage
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception
import java.util.*


class SplashActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var service: Storage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        service = Storage()

        try {
            var aUser = service.getPreferenceKey(this, "user")
            var aPass = service.getPreferenceKey(this, "password")

            if (!TextUtils.isEmpty(aUser) && !TextUtils.isEmpty(aPass)) {

                if (aUser != null && aPass != null) {
                    auth = FirebaseAuth.getInstance()
                    auth.signInWithEmailAndPassword(aUser, aPass).addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                    }
                }

            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }

        } catch (e: Exception) {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

}