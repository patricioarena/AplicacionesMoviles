package com.example.jyc

import Models.Domicilio
import Models.UserDb
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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity(), PostAdapter.OnPublicacionesClickListener, OnFragmentActionsListener {
    private var pressedTime: Long = 0
    private lateinit var service: Facade
    private lateinit var toolbar: Toolbar
    private var db = FirebaseFirestore.getInstance()
    private lateinit var modeloUser : UserDb

    //Para interectuar con la base de datos
    //private lateinit var dbLite: DataBaseHelper
    //private lateinit var userDb: UserDb
    private lateinit var mUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        //notification()

        service = Facade()
        modeloUser = UserDb()


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
        getFirebaseCurrentUser(mUser.uid)
        getCountComentarios(mUser.uid)

        edit_account_btn.setOnClickListener() {
            loadFragment(AccountSettingsFragment())
        }

        images_grid_btn.setOnClickListener(){
            OnclickButtonMyPublish()
        }

    }

    private fun loadFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }

    //ir a comentarios y buscar todos los comentarios de un usuario y hacer count
    private fun getCountComentarios(idUsuario: String?){
        val docRef = db.collection("comentarios").whereEqualTo("idUsuario", idUsuario)
        docRef.get()
                .addOnSuccessListener { documents ->
                    if (documents != null) {
                        var index = 0
                        for (document in documents) {
                            index = index + 1
                        }

                        var cantidadComentarios = index
                        if (cantidadComentarios == null) {
                            cantidadComentarios = 0
                        }

                        modeloUser.cantidadComentarios = cantidadComentarios.toString()
                        total_comentarios.text = modeloUser.cantidadComentarios
                    }
                }
    }

    private fun getFirebaseCurrentUser(idUsuario: String?){
        val docRef = db.collection("usuarios").document(idUsuario!!)
        docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {

                        var myJson = document.data?.get("domicilio").toString()
                        var domicilio = Gson().fromJson(myJson, Domicilio::class.java)
                        var numeroPublicaciones = document.data?.get("publicaciones") as ArrayList<String>?
                        modeloUser.publicaciones = numeroPublicaciones.toString()

                        var cantidadPublicaciones = numeroPublicaciones?.count()
                        if (cantidadPublicaciones == null) {
                            cantidadPublicaciones = 0
                        }

                        modeloUser.cantidadPublicaciones = cantidadPublicaciones.toString()

                        modeloUser.idUsuario = idUsuario
                        modeloUser.nombre = document.data?.get("nombre").toString()
                        modeloUser.apellido = document.data?.get("apellido").toString()
                        modeloUser.calle = domicilio.calle
                        modeloUser.numero = domicilio.numero
                        modeloUser.cp = domicilio.cp
                        modeloUser.ciudad = domicilio.ciudad
                        modeloUser.provincia = domicilio.provincia
                        modeloUser.pais = domicilio.pais
                        modeloUser.email = document.data?.get("email").toString()
                        modeloUser.fechaReg = document.data?.get("fechaReg").toString()
                        modeloUser.fechaNac = document.data?.get("fechaNac").toString()
                        modeloUser.tel = document.data?.get("tel").toString()
                        modeloUser.cel = document.data?.get("cel").toString()


                        var provincia = service.replace20forSpace(modeloUser.provincia!!)
                        var localidad = service.replace20forSpace(modeloUser.ciudad!!)
                        full_name_profile.text = modeloUser.nombre + " " + modeloUser.apellido
                        biografi_profile.text = "Pais: " + modeloUser.pais + "\n" + "Provincia: " + provincia + "\n" + "Localidad: " + localidad
                        total_post.text = modeloUser.cantidadPublicaciones

//                        Log.e(ContentValues.TAG, "DocumentSnapshot data: ${document.data}")
//                        Log.e(ContentValues.TAG, "DocumentSnapshot data nombre: ${document.data?.get("nombre").toString()}")
//                        Log.e(ContentValues.TAG, "DocumentSnapshot data apellido: ${document.data?.get("apellido").toString()}")
//                        Log.e(ContentValues.TAG, "DocumentSnapshot data fechaNac: ${document.data?.get("fechaNac").toString()}")
//                        Log.e(ContentValues.TAG, "DocumentSnapshot data fechaReg: ${document.data?.get("fechaReg").toString()}")
//                        Log.e(ContentValues.TAG, "DocumentSnapshot data email: ${document.data?.get("email").toString()}")


//                        Log.e(ContentValues.TAG, "DocumentSnapshot data calle: ${domicilio.calle}")
//                        Log.e(ContentValues.TAG, "DocumentSnapshot data numero: ${domicilio.numero}")
//                        Log.e(ContentValues.TAG, "DocumentSnapshot data cp: ${domicilio.cp}")
//                        Log.e(ContentValues.TAG, "DocumentSnapshot data ciudad: ${domicilio.ciudad}")
//                        Log.e(ContentValues.TAG, "DocumentSnapshot data provincia: ${domicilio.provincia}")
//                        Log.e(ContentValues.TAG, "DocumentSnapshot data pais: ${domicilio.pais}")

                    } else {
                        Log.e(ContentValues.TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(ContentValues.TAG, "get failed with ", exception)
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

    override fun onClickFragmentButtonAcept() {
        println("ACEPTAR")
    }

    override fun onClickFragmentButtonCancel() {
        replaceFragment(AccountSettingsFragment())
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}

