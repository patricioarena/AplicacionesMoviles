package com.example.jyc

import MyResources.Facade
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_event.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.card_post.*


class HomeActivity : AppCompatActivity(), PostAdapter.OnPublicacionesClickListener {
    private var idUsuario: String? = null
    private var pressedTime: Long = 0
    private lateinit var service: Facade
    private lateinit var toolbar: Toolbar
    private var db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var inFavorites: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //notification()

        service = Facade()

        // Agregar toolbar personalizado a activity main
        toolbar = findViewById(R.id.myToolbar)
        toolbar.title = "Jardines y Cultivos";
        toolbar.subtitle = "Contenido principal del feed";
        setSupportActionBar(toolbar)

        idUsuario = auth.currentUser?.uid

        var publicaciones = mutableListOf<Post>()
        var publicaciones2 = mutableListOf<Post>()

        db.collection("publicaciones").orderBy("fecha", Query.Direction.DESCENDING)
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

                    post.nombreEvento = document.data["nombreEvento"].toString()

                    var losUsuariosQueDieronLike = document.data["likes"] as ArrayList<String>?
                    post.likes = losUsuariosQueDieronLike

                    var losUsuariosQueComentaron =
                        document.data["comentarios"] as ArrayList<String>?
                    post.listaIdcomentarios = losUsuariosQueComentaron

                    if (post.nombreEvento == "null") {
                        post.nombreEvento = ""
                    }

                    var cantidadLikes = losUsuariosQueDieronLike?.count()
                    if (cantidadLikes == null) {
                        cantidadLikes = 0
                    }

                    var cantidadComments = losUsuariosQueComentaron?.count()
                    if (cantidadComments == null) {
                        cantidadComments = 0
                    }


                    post.cantidadDeLikes = cantidadLikes
                    post.cantidadDeComentarios = cantidadComments

                    publicaciones.add(post)
                    //Log.d("TAG", "${document.id} => ${post.post}")

                }

                //Optener nombre y apellido del autor
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
                                recyclerView.layoutManager = LinearLayoutManager(this)
                                recyclerView.addItemDecoration(
                                    DividerItemDecoration(
                                        this,
                                        DividerItemDecoration.VERTICAL
                                    )
                                )
                                recyclerView.adapter = PostAdapter(this, temp, this)
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
            R.id.nav_testActivity -> {
                startActivity(Intent(this, MyFunActivity::class.java))
                return true
            }
            R.id.nav_new_event -> {
                startActivity(Intent(this, NewEventActivity::class.java))
                return true
            }
            R.id.nav_new_pub -> {
                startActivity(Intent(this, PublicationActivity::class.java))
                return true
            }
            R.id.nav_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                return true
            }
            R.id.nav_logout -> {
                logoutUser()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onImageClick(image: String?) {

        var intent = Intent(this, ImagenDetail::class.java)
        intent.putExtra("imageUrl", image)
        startActivity(intent)
    }

    override fun onItemClick(idUsuario: String?) {
//        Toast.makeText(this, idUsuario, Toast.LENGTH_SHORT).show();
    }

    override fun onMoreInfoClick(item: Post) {
        val intent: Intent = Intent(this, EventActivity::class.java)
            .putExtra("item.uid", item.uid)

        startActivity(intent)
    }

    override fun onFavClick(uid: String?) {

        println(uid)
        Toast.makeText(this, "${uid}", Toast.LENGTH_SHORT).show();
        if (inFavorites == false){
            addFavorites(idUsuario,uid)
            inFavorites=true
        }
        else {
            removeFavorites(idUsuario,uid)
            inFavorites=false
        }
    }

    private fun addFavorites(idUsuario: String?,uid: String?){
        db.collection("usuarios").document(idUsuario!!)
            .update("favoritos", FieldValue.arrayUnion(uid))
            .addOnSuccessListener {
                Toast.makeText(this, "Agregado a favoritos", Toast.LENGTH_SHORT).show();
//                fav_btn.setImageResource(R.drawable.ic_baseline_star_fav_24)
                fav_btn.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.firebase_amarillo));
            }
    }

    private fun removeFavorites(idUsuario: String?,uid: String?) {
        db.collection("usuarios").document(idUsuario!!)
            .update( "favoritos", FieldValue.arrayRemove(uid))
            .addOnSuccessListener {
                Toast.makeText(this, "Eliminado de favoritos", Toast.LENGTH_SHORT).show();
//                fav_btn.setImageResource(R.drawable.ic_baseline_star_24)
                fav_btn.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.firebase_azul));
            }
    }

}