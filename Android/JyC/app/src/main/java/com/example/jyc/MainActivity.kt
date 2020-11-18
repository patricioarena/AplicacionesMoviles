package com.example.jyc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import MyResources.Storage
import android.content.Intent
import android.os.Handler
import android.text.TextUtils
import com.google.firebase.auth.FirebaseAuth

// Actividad encargadar de redireccionar a las actividades,
// login o main dependiento de si hay o no credenciales almacenadas
class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var service: Storage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        service = Storage()
        var main = Intent(this, HomeActivity::class.java)
        var login = Intent(this, LoginActivity::class.java)

        try {
            var aUser = service.getPreferenceKey(this, "user")
            var aPass = service.getPreferenceKey(this, "password")

            if (!TextUtils.isEmpty(aUser) && !TextUtils.isEmpty(aPass)) {

                if (aUser != null && aPass != null) {
                    auth = FirebaseAuth.getInstance()
                    auth.signInWithEmailAndPassword(aUser, aPass).addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
//            startActivity(main)
                            routeAfterTime(main,500)
                        }else{
                            service.deletePreferenceKey(this,"user")
                            service.deletePreferenceKey(this,"password")
//            startActivity(login)
                            routeAfterTime(login,500)
                        }
                    }
                }
            } else {
                service.deletePreferenceKey(this,"user")
                service.deletePreferenceKey(this,"password")
//            startActivity(login)
                routeAfterTime(login,500)
            }
        } catch (e: Exception) {
            // Aca deberia dar la opcion de enviar informe de errores
            service.deletePreferenceKey(this,"user")
            service.deletePreferenceKey(this,"password")
//            startActivity(login)
            routeAfterTime(login,500)
        }
    }

    // Ruteo a la actividad indicada despues de un tiempo dado
    fun routeAfterTime(intent: Intent,time:Int) {
        Handler().postDelayed({
            startActivity(intent)
            finish()
        }, time.toLong())
    }

}