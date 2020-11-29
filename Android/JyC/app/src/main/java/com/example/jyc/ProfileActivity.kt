package com.example.jyc

import Models.UserDb
import MyResources.DataBaseHelper
import MyResources.Facade
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity(), PostAdapter.OnPublicacionesClickListener {
    private var pressedTime: Long = 0
    private lateinit var service: Facade
    private lateinit var toolbar: Toolbar
    private var db = FirebaseFirestore.getInstance()

    //Para interectuar con la base de datos
    //private lateinit var dbLite: DataBaseHelper
    //private lateinit var userDb: UserDb
    private lateinit var mUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        //notification()

        service = Facade()

        //Obtenemos el idUsruario de las preferencias que se guardo al hacer login
        //idUsuario = service.getPreferenceKey(this, "idUsuario").toString()

        // Inicializamos una instancia de la base de datos
        //dbLite  = DataBaseHelper(this)

        //Obtenemos el usuario
        //userDb = dbLite.readData(idUsuario)!!

        mUser = FirebaseAuth.getInstance().currentUser!!

        // Agregar toolbar personalizado a activity main
        toolbar = findViewById(R.id.myToolbar)
        toolbar.title = "Jardines y Cultivos";
        toolbar.subtitle = "Mi Perfil";
        setSupportActionBar(toolbar)

        OnclickButtonMyPublish ()

        images_grid_btn.setOnClickListener(){
            OnclickButtonMyPublish()
        }

    }

    private fun OnclickButtonMyPublish (){

        var publicaciones = mutableListOf<Post>()
        var publicaciones2 = mutableListOf<Post>()


        db.collection("publicaciones").whereEqualTo("idUsuario", mUser.uid)
                .addSnapshotListener { querySnapshot: QuerySnapshot?, firebaseFirestoreException: FirebaseFirestoreException? ->

                    if (firebaseFirestoreException != null) {
                        Log.w(ContentValues.TAG, "Listen failed.", firebaseFirestoreException)
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

                        var losUsuariosQueComentaron = document.data["comentarios"] as ArrayList<String>?
                        post.listaIdcomentarios = losUsuariosQueComentaron

                        var cantidadLikes = losUsuariosQueDieronLike?.count()
                        if (cantidadLikes == null) {
                            cantidadLikes = 0
                        }

                        var cantidadComments = losUsuariosQueComentaron?.count()
                        if (cantidadComments == null){
                            cantidadComments = 0
                        }


                        post.cantidadDeLikes = cantidadLikes
                        post.cantidadDeComentarios = cantidadComments

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
                                        recycler_view_user_profile.layoutManager = LinearLayoutManager(this)
                                        recycler_view_user_profile.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
                                        recycler_view_user_profile.adapter = PostAdapter(this,temp,this)
                                    }
                                }
                    }
                }
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
                startActivity(Intent(this, EventActivity::class.java))
                return true
            }
            R.id.nav_new_pub -> {
                startActivity(Intent(this, PublicationActivity::class.java))
                return true
            }
            R.id.nav_logout -> {
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
        Toast.makeText(this, idUsuario, Toast.LENGTH_SHORT).show();
    }

}