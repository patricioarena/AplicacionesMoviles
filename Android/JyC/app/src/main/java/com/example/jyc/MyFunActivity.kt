package com.example.jyc

import MyResources.Facade
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import org.json.JSONObject


class MyFunActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var service: Facade

    private lateinit var button_post: Button
    private lateinit var button_susc_online: Button
    private lateinit var button_susc_presencial: Button
    private lateinit var button_des_susc_online: Button
    private lateinit var button_des_susc_presencial: Button

    private lateinit var radioButton_Online: RadioButton
    private lateinit var radioButton_Presencial: RadioButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_fun)

        // Agregar toolbar personalizado a activity main
        toolbar = findViewById(R.id.myToolbar)
        toolbar.title = "FuntionActivity";
        toolbar.subtitle = "Test functions DEV";

        setSupportActionBar(toolbar)
        service = Facade()

        button_post = findViewById(R.id.button_post)

        var scope:String? = null
        radioButton_Online = findViewById(R.id.radioButton_Online)

        radioButton_Online.setSelected(true)
        radioButton_Online.setOnClickListener{
            scope = EventActivity.Evento.ONLINE.toString()
        }


        radioButton_Presencial = findViewById(R.id.radioButton_Presencial)
        radioButton_Presencial.setOnClickListener{
            scope = EventActivity.Evento.PRESENCIAL.toString()
        }



        button_susc_online = findViewById(R.id.button_susc_online)
        button_susc_online.setOnClickListener{
            subscripcion(scope!!)
        }


        button_susc_presencial = findViewById(R.id.button_susc_presencial)
        button_susc_presencial.setOnClickListener{
            subscripcion(scope!!)
        }







        button_post.setOnClickListener {

            val jsonObject = JSONObject(
                    """{
                  "to": "/topics/${scope}",
                  "notification": {
                    "title": "Noticia para evento ONLINE",
                    "body": "Descripción de la noticia desde el Otro celular"
                  },
                  "data": {
                    "titulo": "Este es el titular",
                    "descripcion": "Aquí estará todo el contenido de la noticia"
                  }
                }"""
            )

            getRemoteConfigAndSendNotificationCloudMessaging(jsonObject);
        }
    }


    private fun logoutUser() {
        service.deletePreferenceKey(this, "token")
        super.finishAffinity()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_toobar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.menu_settings -> {
                Toast.makeText(this, "Mover a activity Settings", Toast.LENGTH_SHORT).show();
                return true
            }
            R.id.nav_home -> {
                startActivity(Intent(this, HomeActivity::class.java))
                return true
            }
            R.id.nav_testActivity -> {
                startActivity(Intent(this, MyFunActivity::class.java))
                return true
            }
            R.id.nav_new_pub -> {
                startActivity(Intent(this, PublicationActivity::class.java))
                return true
            }
            R.id.nav_logout -> {
                logoutUser()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    class CustomJsonObjectRequestBasicAuth(
            method: Int, url: String,
            jsonObject: JSONObject?,
            listener: Response.Listener<JSONObject>,
            errorListener: Response.ErrorListener,
            credentials: String
    ) : JsonObjectRequest(method, url, jsonObject, listener, errorListener) {

        private var mCredentials: String = credentials

        @Throws(AuthFailureError::class)
        override fun getHeaders(): Map<String, String> {
            val auth = mCredentials
            val headers = HashMap<String, String>()
            headers["Content-Type"] = "application/json"
            headers["Authorization"] = auth
            return headers
        }
    }


    fun sendNotificationCloudMessaging(url: String, jsonObject: JSONObject?, credentials: String) {
        // Make a volley custom json object request with basic authentication
        val request = CustomJsonObjectRequestBasicAuth(Request.Method.POST, url, jsonObject,
                { response ->
                    try {
                        // Parse the json object here
                        println("Response : $response")
                    } catch (e: Exception) {
                        e.printStackTrace()
                        println("Parse exception : $e")
                    }
                }, {
            println("Volley error: $it}")
        }, credentials
        )

        // Add the volley request to request queue
        VolleySingleton.getInstance(this).addToRequestQueue(request)
    }


    fun getRemoteConfigAndSendNotificationCloudMessaging(jsonObject: JSONObject?): String? {
        var value: String? = null
        var remoteConfig = FirebaseRemoteConfig.getInstance()
        remoteConfig.fetchAndActivate()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        var credentials = remoteConfig.getString("Authorization")
//                    Log.d("TAG", "Config params updated: $value")
//                    Toast.makeText(this, "Fetch and activate succeeded", Toast.LENGTH_SHORT).show
                        sendNotificationCloudMessaging("https://fcm.googleapis.com/fcm/send", jsonObject, credentials)

                    } else {
                        Log.e("Error", task.exception.toString())
                    }

                }
        return value
    }

    private fun subscripcion(topic:String){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            Log.e(ContentValues.TAG, token)
            Toast.makeText(baseContext, "Token obtenido", Toast.LENGTH_SHORT).show()
        })

        FirebaseMessaging.getInstance().subscribeToTopic(topic)
//        FirebaseMessaging.getInstance().subscribeToTopic("PRESENCIAL")
    }

}