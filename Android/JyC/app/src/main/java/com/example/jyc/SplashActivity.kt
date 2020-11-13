package com.example.jyc

import MyResources.Storage
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


// Actividad encargadar de redireccionar a las actividades,
// login o main dependiento de si hay o no credenciales almacenadas
class SplashActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var service: Storage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        service = Storage()
        var main = Intent(this, MainActivity::class.java)
        var login = Intent(this, LoginActivity::class.java)

        try {
            var aUser = service.getPreferenceKey(this, "user")
            var aPass = service.getPreferenceKey(this, "password")

            if (!TextUtils.isEmpty(aUser) && !TextUtils.isEmpty(aPass)) {

                if (aUser != null && aPass != null) {
                    auth = FirebaseAuth.getInstance()
                    auth.signInWithEmailAndPassword(aUser, aPass).addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            routeAfterTime(main,250)
                        }else{
                            service.deletePreferenceKey(this,"user")
                            service.deletePreferenceKey(this,"password")
                            routeAfterTime(login,500)
                        }
                    }
                }
            } else {
                service.deletePreferenceKey(this,"user")
                service.deletePreferenceKey(this,"password")
                routeAfterTime(login,500)
            }
        } catch (e: Exception) {
            // Aca deberia dar la opcion de enviar informe de errores
            service.deletePreferenceKey(this,"user")
            service.deletePreferenceKey(this,"password")
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