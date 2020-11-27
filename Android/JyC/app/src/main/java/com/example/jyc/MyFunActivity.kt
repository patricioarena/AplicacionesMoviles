package com.example.jyc

import MyResources.Facade
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class MyFunActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var service: Facade
    private lateinit var button_post: Button

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
        button_post.setOnClickListener{
            //val credentials = "username:$password"
            val credentials = "postman:password"
            val url = "https://fcm.googleapis.com/fcm/send"

            val jsonObject = JSONObject("""{
  "to": "/topics/ONLINE",
  "notification": {
    "title": "Noticia para evento ONLINE",
    "body": "Descripción de la noticia desde el Otro celular"
  },
  "data": {
    "titulo": "Este es el titular",
    "descripcion": "Aquí estará todo el contenido de la noticia"
  }
}""")

            // Make a volley custom json object request with basic authentication
            val request = CustomJsonObjectRequestBasicAuth(Request.Method.POST, url,jsonObject,
                { response->
                    try {
                        // Parse the json object here
                        println("Response : $response")
                    }catch (e:Exception){
                        e.printStackTrace()
                        println("Parse exception : $e")
                    }
                }, {
                    println("Volley error: $it}")
                },credentials
            )

            // Add the volley request to request queue
            VolleySingleton.getInstance(this).addToRequestQueue(request)
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
     method:Int, url: String,
     jsonObject: JSONObject?,
     listener: Response.Listener<JSONObject>,
     errorListener: Response.ErrorListener,
     credentials:String
)
    : JsonObjectRequest(method,url, jsonObject, listener, errorListener) {

    private var mCredentials:String = credentials

    @Throws(AuthFailureError::class)
    override fun getHeaders(): Map<String, String> {
        val headers = HashMap<String, String>()
        headers["Content-Type"] = "application/json"
        //val credentials:String = "username:password"
        val auth = "key=AAAA9wJXDVo:APA91bFkgIsY3ot--5sdu5Ep94TWzg8tJZAEgRSCg2m6xmeFgNxuht6EYEj3-1pDbl9wnn1FVGrbGe0-zaNuxQVZYV_fIiUE-rmZTJ4wUSeEXlFfxRqiSJfrj2vWDa31vVEiNNIkz-t0\t\n"
        headers["Authorization"] = auth
        return headers
    }
}
}