package com.example.jyc

import Models.Domicilio
import MyResources.Facade
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_event.*

class EventActivity : AppCompatActivity() {
    private var inFavorites: Boolean? = false
    private lateinit var toolbar: Toolbar
    private lateinit var service: Facade
    private var db = FirebaseFirestore.getInstance()
    private var post = Post()
    private var domicilio = Domicilio()
    private val auth = FirebaseAuth.getInstance()
    private lateinit var fechaEvento: String
    private lateinit var horaEvento: String
    private lateinit var eventoLink: String
    private var presencial: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        service = Facade()
        // Agregar toolbar personalizado a activity main
        toolbar = findViewById(R.id.myToolbar)
        toolbar.title = "Jardines y Cultivos";
        toolbar.subtitle = "Evento"

        setSupportActionBar(toolbar)

        var uid = intent.getStringExtra("item.uid")
        var idUsuario = auth.currentUser?.uid
        getFavorites(idUsuario,uid)


        post = Post()
        getPublication(uid)

        Event_share_btn.setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, post.post)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        Event_fav_btn.setOnClickListener {

            if (inFavorites == false){
                addFavorites(idUsuario)
                inFavorites=true
            }
            else {
                removeFavorites(idUsuario)
                inFavorites=false
            }
        }

        Event_comment_btn.setOnClickListener {
            val commentIntent = Intent(this, CommentsActivity::class.java)
            commentIntent.putExtra("postId", post.uid)
            commentIntent.putExtra("publicacionId", post.post)
            commentIntent.putExtra("userName", post.userName)
            commentIntent.putExtra("imagen", post.image)
            commentIntent.putExtra("idUsuario", post.idUsuario)
            //paso los id de comentarios que tiene una publicacion
            commentIntent.putStringArrayListExtra("listaIdcomentarios", post.listaIdcomentarios)
            startActivity(commentIntent)
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

    private fun getPublication(uid: String?) {
        db.collection("publicaciones").document(uid!!).get().addOnSuccessListener { result ->
            val document = result
            post.uid = document.id
            post.idUsuario = document.data?.get("idUsuario")?.toString()
            post.date = document.data?.get("fecha")?.toString()
            post.post = document.data?.get("articulo")?.toString()
            post.image = document.data?.get("imagen")?.toString()
            post.nombreEvento = document.data?.get("nombreEvento")?.toString()

            fechaEvento = document.data?.get("fechaEvento")?.toString().toString()
            horaEvento = document.data?.get("hora")?.toString().toString()
            eventoLink = ""

            var losUsuariosQueDieronLike = document.data?.get("likes") as ArrayList<String>?
            post.likes = losUsuariosQueDieronLike

            var losUsuariosQueComentaron = document.data?.get("comentarios") as ArrayList<String>?
            post.listaIdcomentarios = losUsuariosQueComentaron

            var categorias = document.data?.get("categorias") as ArrayList<String>?

            println(categorias)

            if (categorias?.contains("Evento PRESENCIAL")!!) {
                presencial = true;
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

            if (categorias.contains("Evento PRESENCIAL") || categorias.contains("Evento ONLINE")) {
                if (presencial) {

                    var myJson = document.data?.get("lugar").toString()
                    var dom = Gson().fromJson(myJson, Domicilio::class.java)

                    domicilio.calle = dom.calle
                    domicilio.cp = dom.cp
                    domicilio.localidad = dom.localidad
                    domicilio.provincia = dom.provincia
                    domicilio.numero = dom.numero
                    domicilio.pais = dom.pais
                    domicilio.piso = dom.piso
                    domicilio.eCalle_1 = dom.eCalle_1
                    domicilio.eCalle_2 = dom.eCalle_2
                    layout_presencial.setVisibility(View.VISIBLE)
                    layout_online.setVisibility(View.GONE)
                    Event_editTextFecha.setVisibility(View.VISIBLE)
                    Event_editTextHora.setVisibility(View.VISIBLE)
                } else {
                    eventoLink = document.data?.get("url")?.toString().toString()
                    layout_online.setVisibility(View.VISIBLE)
                    layout_presencial.setVisibility(View.GONE)
                    Event_editTextFecha.setVisibility(View.VISIBLE)
                    Event_editTextHora.setVisibility(View.VISIBLE)
                }
            } else {
                layout_presencial.setVisibility(View.GONE)
                layout_online.setVisibility(View.GONE)
                Event_editTextFecha.setVisibility(View.GONE)
                Event_editTextHora.setVisibility(View.GONE)
            }

        }.addOnCompleteListener {

            db.collection("usuarios")
                .document(post.idUsuario!!).get()
                .addOnSuccessListener { result ->
                    val document = result
                    post.userName = document?.data?.get("nombre")
                        .toString() + " " + document?.data?.get("apellido").toString()
                }.addOnCompleteListener {

                    Event_CardTitle.text = post.nombreEvento
                    Event_username_tv.text = post.userName
                    Event_post_tv.text = post.post
                    Picasso.get().load(post.image).into(Event_image_tv)
                    Event_likesCount_tv.text = "${post.cantidadDeLikes} likes"
                    Event_fecha_tv.text = post.date
                    Event_commentsCount_tv.text = "${post.cantidadDeComentarios} comentarios"

                    if (presencial) {
                        Event_editTextNumero.text = "Numero: ${domicilio.numero.toString()}"
                        Event_editTextPiso.text = "Piso: ${domicilio.piso}"
                        Event_editTextLocalidad.text = "Localidad: ${service.replace20forSpace(domicilio.localidad.toString())}"
                        Event_editTextProvincia.text = "Provincia: ${service.replace20forSpace(domicilio.provincia.toString())}"
                        Event_editTextCalle.text = "Calle: ${service.replace20forSpace(domicilio.calle.toString())}"
                        Event_editTextEntreCalle1.text = "Entre calle 1: ${service.replace20forSpace(domicilio.eCalle_1.toString())}"
                        Event_editTextEntreCalle2.text = "Entre calle 2: ${service.replace20forSpace(domicilio.eCalle_2.toString())}"
                        Event_editTextCp.text = "Cod. Postal: ${service.replace20forSpace(domicilio.cp.toString())}"
                        Event_editTextPais.text = "Pais: ${service.replace20forSpace(domicilio.pais.toString())}"
                    } else {
                        Event_link.text = eventoLink
                    }

                    Event_editTextFecha.text = "Fecha: ${fechaEvento}"
                    Event_editTextHora.text = "Hora: ${horaEvento}"

                }

        }
    }

    private fun addFavorites(idUsuario: String?){
        db.collection("usuarios").document(idUsuario!!)
            .update("favoritos", FieldValue.arrayUnion(post.uid))
            .addOnSuccessListener {
                Toast.makeText(this, "Agregado a favoritos", Toast.LENGTH_SHORT).show();
//                Event_fav_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_star_fav_24, 0, 0, 0);
                Event_fav_btn.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.firebase_amarillo));
            }
    }

    private fun removeFavorites(idUsuario: String?) {
            db.collection("usuarios").document(idUsuario!!)
                .update( "favoritos", FieldValue.arrayRemove(post.uid))
                .addOnSuccessListener {
                    Toast.makeText(this, "Eliminado de favoritos", Toast.LENGTH_SHORT).show();
//                    Event_fav_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_star_24, 0, 0, 0);
                    Event_fav_btn.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.firebase_azul));
                }
        }

    private fun getFavorites(idUsuario: String?,idPublicacion: String?){
        db.collection("usuarios").document(idUsuario!!).get()
            .addOnSuccessListener { result ->
                val document = result
                var fav = document.data?.get("favoritos") as  ArrayList<String>?

                inFavorites = fav?.contains(idPublicacion.toString())

                if (inFavorites == false) {
//                   Event_fav_btn.setImageResource(R.drawable.ic_baseline_star_24);
                   Event_fav_btn.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.firebase_azul));
                }else{
//                    Event_fav_btn.setImageResource(R.drawable.ic_baseline_star_fav_24);
                    Event_fav_btn.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.firebase_amarillo));
                }

            }
    }


}

