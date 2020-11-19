package com.example.jyc

import MyResources.Storage
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.auth0.android.jwt.JWT
import com.google.firebase.auth.FirebaseAuth
import java.util.*

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
            var aToken = service.getPreferenceKey(this, "token")

            if (!TextUtils.isEmpty(aToken)) {
                if (aToken != null){
                    val jwt = JWT(aToken)
                    if (!jwt.isExpired(10)){
                       routeAfterTime(main,500)
                    }else{
                       routeAfterTime(login,500)
                    }
                }

            } else {
                service.deletePreferenceKey(this, "token")

                routeAfterTime(login, 500)
            }
        } catch (e: Exception) {
            // Aca deberia dar la opcion de enviar informe de errores
            service.deletePreferenceKey(this, "token")
            routeAfterTime(login, 500)
        }
    }

    // Ruteo a la actividad indicada despues de un tiempo dado
    fun routeAfterTime(intent: Intent, time: Int) {
        Handler().postDelayed({
            startActivity(intent)
            finish()
        }, time.toLong())
    }

}