package com.example.jyc

import MyResources.Facade
import android.content.Intent

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*
import android.content.ContentValues.TAG
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.card_post.*
import java.lang.Exception
import java.lang.reflect.Array
import kotlin.collections.ArrayList


class HomeActivity : AppCompatActivity() {
    private var pressedTime: Long = 0
    private lateinit var service: Facade
    private lateinit var toolbar: Toolbar
    private var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        service = Facade()

        // Agregar toolbar personalizado a activity main
        toolbar = findViewById(R.id.myToolbar)
        toolbar.title = "HomeActivity";
        toolbar.subtitle = "Contenido principal del feed";
        setSupportActionBar(toolbar)

        //        val post = Post("Hola mundo", Date(), "Ayelen", "https://picsum.photos/200/300?grayscale")
//        val post2 = Post("Hola mundo", Date(), "Esteban Quito", "https://picsum.photos/200/300?grayscale")
//        val post3 = Post("Hola mundo", Date(), "Patricio", "https://picsum.photos/200/300?grayscale")
//        val post4 = Post("Hola mundo", Date(), "Ayelen", "https://picsum.photos/200/300?grayscale")
//        val post5 = Post("Hola mundo", Date(), "Armando Esteban Quito", "https://picsum.photos/200/300?grayscale")
//
//        val posts = listOf(post,post2,post3,post4,post5)

        var publicaciones = mutableListOf<Post>()
        var publicaciones2 = mutableListOf<Post>()

        db.collection("publicaciones")
                .addSnapshotListener { querySnapshot: QuerySnapshot?, firebaseFirestoreException: FirebaseFirestoreException? ->

                    if (firebaseFirestoreException != null) {
                        Log.w(TAG, "Listen failed.", firebaseFirestoreException)
                        return@addSnapshotListener
                    }

                    publicaciones.clear()
                    publicaciones2.clear()

                    for (document in querySnapshot!!) {
//                        Log.d("TAG", "${document.id} => ${document.data}")

                        var post = Post()
                        post.uid = document.id
                        post.idUsuario = document.data["idUsuario"].toString()
                        post.date = document.data["fecha"].toString()
                        post.post = document.data["articulo"].toString()
                        post.image = document.data["imagen"].toString()

                        var losUsuariosQueDieronLike = document.data["likes"] as ArrayList<String>?
                        post.likes = losUsuariosQueDieronLike

                        var cantidad = losUsuariosQueDieronLike?.count()
                        if (cantidad == null) {
                            cantidad = 0
                        }

                        post.cantidadDeLikes = cantidad

                        publicaciones.add(post)
                        //Log.d("TAG", "${document.id} => ${post.post}")

                    }

                    for (pub in publicaciones) {
                        db.collection("usuarios").document(pub.idUsuario.toString()).get()
                                .addOnSuccessListener { result ->
                                    val document = result
                                    pub.userName = document?.data?.get("nombre").toString() + " " + document?.data?.get("apellido").toString()
                                    publicaciones2.add(pub)
                                }
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        var temp = publicaciones2.toList();
                                        recyclerView.apply {
                                            setHasFixedSize(true)
                                            layoutManager = LinearLayoutManager(this@HomeActivity)
                                            adapter = PostAdapter(this@HomeActivity, temp)
                                        }
                                    }
                                }
                    }
                }

    }


    // Si la autenticacion es satisfactoria guardamos el token
    // por lo tanto la implementacion de la funcionalidad de logout
    // se basa en el paso inverso que es borrar el token y posteriormente cerrar la aplicacion
    // tambien podriamos reenviar al usuario al login
    private fun logoutUser() {
        service.deletePreferenceKey(this, "token")
        super.finishAffinity()
    }

    // Sobrescribimos el metodo que la salida de la aplicacion sea mas agil
    // de no ser asi volverimos a la actividad Splash y tendriamos que salir desde ahi
    override fun onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.finishAffinity()
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }

        pressedTime = System.currentTimeMillis();
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
            R.id.nav_gallery -> {
                Toast.makeText(this, "Mover a activity Gallery", Toast.LENGTH_SHORT).show();
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


}