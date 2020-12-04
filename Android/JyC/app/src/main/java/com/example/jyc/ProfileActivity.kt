package com.example.jyc

import Models.Domicilio
import Models.UserDb
import MyResources.Facade
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_comments.*
import kotlinx.android.synthetic.main.activity_event.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.card_post.*
import kotlinx.android.synthetic.main.fragment_account_settings.*
import kotlinx.android.synthetic.main.fragment_account_settings.view.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList

class ProfileActivity : AppCompatActivity(), PostAdapter.OnPublicacionesClickListener, OnFragmentActionsListener {
    private lateinit var avatar: String
    private lateinit var service: Facade
    private lateinit var toolbar: Toolbar
    private var db = FirebaseFirestore.getInstance()
    private lateinit var modeloUser: UserDb
    private var inFavorites: Boolean? = false
    private lateinit var bundle: Bundle

    //Para interectuar con la base de datos
    //private lateinit var dbLite: DataBaseHelper
    //private lateinit var userDb: UserDb
    private lateinit var mUser: FirebaseUser
    private var mStorageRef: StorageReference? = null
    private var bitmap: Bitmap? = null

    //image pick code
    private val IMAGE_PICK_CODE = 1000;

    //Permission code
    private val PERMISSION_CODE = 1001;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        //notification()

        service = Facade()
        modeloUser = UserDb()
        mUser = FirebaseAuth.getInstance().currentUser!!
        mStorageRef = FirebaseStorage.getInstance().getReference();


        // Agregar toolbar personalizado a activity main
        toolbar = findViewById(R.id.myToolbar)
        toolbar.title = "Jardines y Cultivos";
        toolbar.subtitle = "Mi Perfil";
        setSupportActionBar(toolbar)

        avatar = service.getPreferenceKey(this, "avatar").toString()

        if (!TextUtils.isEmpty(avatar)) {
            Picasso.get().load(avatar).into(profile_image)
        }

        //Obtenemos el idUsruario de las preferencias que se guardo al hacer login
        //idUsuario = service.getPreferenceKey(this, "idUsuario").toString()

        // Inicializamos una instancia de la base de datos
        //dbLite  = DataBaseHelper(this)

        //Obtenemos el usuario
        //userDb = dbLite.readData(idUsuario)!!

        OnclickButtonMyPublish()
        getFirebaseCurrentUser(mUser.uid)
        getCountComentarios(mUser.uid)

        edit_account_btn.setOnClickListener() {
            loadFragment(AccountSettingsFragment())
        }

        images_grid_btn.setOnClickListener() {
            OnclickButtonMyPublish()
        }

        images_save_btn.setOnClickListener() {

        }

    }

    @SuppressLint("WrongViewCast")
    private fun loadFragment(fragment: Fragment) {
        fragmentContainer.setVisibility(View.VISIBLE)
        scroll_view_profile.setVisibility(View.GONE)

        fragment.arguments = bundle

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }

    private fun hideFragment(fragment: Fragment) {
        fragmentContainer.setVisibility(View.GONE)
        scroll_view_profile.setVisibility(View.VISIBLE)
        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (fragment != null) {
            println("Muereee!!")
            supportFragmentManager.beginTransaction().remove(fragment).commit()
        }
    }

    //ir a comentarios y buscar todos los comentarios de un usuario y hacer count
    private fun getCountComentarios(idUsuario: String?) {
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

    private fun getFirebaseCurrentUser(idUsuario: String?) {
        db.collection("usuarios").document(idUsuario!!).get()
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
                    modeloUser.localidad = domicilio.localidad
                    modeloUser.provincia = domicilio.provincia
                    modeloUser.pais = domicilio.pais
                    modeloUser.email = document.data?.get("email").toString()
                    modeloUser.fechaReg = document.data?.get("fechaReg").toString()
                    modeloUser.fechaNac = document.data?.get("fechaNac").toString()
                    modeloUser.tel = document.data?.get("tel").toString()
                    modeloUser.cel = document.data?.get("cel").toString()

                    var provincia = service.replace20forSpace(modeloUser.provincia!!)
                    var localidad = service.replace20forSpace(modeloUser.localidad!!)
                    full_name_profile.text = modeloUser.nombre + " " + modeloUser.apellido
                    biografi_profile.text = "Pais: " + modeloUser.pais + "\n" + "Provincia: " + provincia + "\n" + "Localidad: " + localidad
                    total_post.text = modeloUser.cantidadPublicaciones

                    bundle = Bundle()
                    bundle.putString("calle", service.replace20forSpace(modeloUser.calle.toString()))
                    bundle.putString("numero", modeloUser.numero.toString())
                    bundle.putString("cp", modeloUser.cp.toString())
                    bundle.putString("localidad", service.replace20forSpace(modeloUser.localidad.toString()))
                    bundle.putString("provincia", service.replace20forSpace(modeloUser.provincia.toString()))
                    bundle.putString("pais", service.replace20forSpace(modeloUser.pais.toString()))
                    bundle.putString("cel", modeloUser.cel)
                    bundle.putString("tel", modeloUser.tel)
                    bundle.putString("avatar", avatar)

//                    if (modeloUser.calle != null)
//                        calle_user.setText(modeloUser.calle)

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

    private fun OnclickButtonMyPublish() {

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
                                recycler_view_user_profile.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
                                recycler_view_user_profile.adapter = PostAdapter(this, temp, this)
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

    private fun logoutUser() {
        service.deletePreferenceKey(this, "token")
        super.finishAffinity()
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
        Toast.makeText(this, idUsuario, Toast.LENGTH_SHORT).show();
    }

    override fun onMoreInfoClick(item: Post) {
        val intent: Intent = Intent(this, EventActivity::class.java)
            .putExtra("item.uid", item.uid)

        startActivity(intent)
    }

    override fun onFavClick(uid: String?) {
        if (inFavorites == false) {
            addFavorites(mUser.uid, uid)
            inFavorites = true
        } else {
            removeFavorites(mUser.uid, uid)
            inFavorites = false
        }
    }

    override fun onClickFragmentButtonAcept() {
        println("ACEPTAR")
        uploadEvent()
    }

    override fun onClickFragmentButtonCancel() {
        println("CANCELAR")
        hideFragment(AccountSettingsFragment())
    }

    override fun onClickFragmentImage() {
        println("onClickFragmentImage")

        //check runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED
            ) {
                //permission denied
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                //show popup to request runtime permission
                requestPermissions(permissions, PERMISSION_CODE);
            } else {
                //permission already granted
                pickImageFromGallery();
            }
        } else {
            //system OS is < Marshmallow
            pickImageFromGallery();
        }

    }


    private fun addFavorites(idUsuario: String?, uid: String?) {
        db.collection("usuarios").document(idUsuario!!)
            .update("favoritos", FieldValue.arrayUnion(uid))
            .addOnSuccessListener {
                Toast.makeText(this, "Agregado a favoritos", Toast.LENGTH_SHORT).show();
//                fav_btn.setImageResource(R.drawable.ic_baseline_star_fav_24)
                fav_btn.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.firebase_amarillo));
            }
    }

    private fun removeFavorites(idUsuario: String?, uid: String?) {
        db.collection("usuarios").document(idUsuario!!)
            .update("favoritos", FieldValue.arrayRemove(uid))
            .addOnSuccessListener {
                Toast.makeText(this, "Eliminado de favoritos", Toast.LENGTH_SHORT).show();
//                fav_btn.setImageResource(R.drawable.ic_baseline_star_24)
                fav_btn.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.firebase_azul));
            }
    }


    private fun uploadEvent() {

        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        val user: FirebaseUser? = mUser
        var idUsuario = user?.uid.toString()

        val stream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val random: String = UUID.randomUUID().toString()
        val b: ByteArray = stream.toByteArray()

        if (b.size == 0) {

            println("b = 0, actualizar solo datos")
            var imagenUri = "https://firebasestorage.googleapis.com/v0/b/jyc-appa.appspot.com/o/user-logos-user-logo-png-1920_1280.png?alt=media&token=311b1b6f-b51f-4adb-8a4d-98c7d5aa893d"
            if (avatar !=imagenUri){
               imagenUri = avatar
            }
            updatePerfil(imagenUri,idUsuario)

        } else {
            println("b != 0, actualiza imagen y datos ")
            val imageRef: StorageReference? = mStorageRef?.child("$idUsuario/$random")
            if (imageRef != null) {
                imageRef.putBytes(b)
                    .addOnSuccessListener { taskSnapshot ->
                        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                            updatePerfil(uri.toString(),idUsuario)
                        }
                    }
                    .addOnFailureListener {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        Toast.makeText(this, "Upload Failed", Toast.LENGTH_SHORT).show();
                    }
            }
        }


    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK && data != null) {

            val selectedPhotoUri = data.data
            try {
                selectedPhotoUri?.let {
                    if (Build.VERSION.SDK_INT < 28) {
                        val bitmapx = MediaStore.Images.Media.getBitmap(
                            this.contentResolver,
                            selectedPhotoUri
                        )
                        profile_image_setting.setImageBitmap(bitmapx)
                    } else {
                        val source = ImageDecoder.createSource(this.contentResolver, selectedPhotoUri)
                        val bitmapx = ImageDecoder.decodeBitmap(source)
                        bitmap = bitmapx
                        profile_image_setting.setImageBitmap(bitmapx)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun updatePerfil(imagenUri: String?, idUsuario: String?){
        service.setPreferenceKey(this, "avatar", imagenUri)

        var temp = mapOf(
            "domicilio.calle" to service.removeSpaces(calle_user.text.toString()),
            "domicilio.numero" to calle_numero_user.text.toString(),
            "domicilio.cp" to cp_user.text.toString(),
            "domicilio.localidad" to service.removeSpaces(ciudad_user.text.toString()),
            "domicilio.provincia" to service.removeSpaces(provincia_user.text.toString()),
            "cel" to cel_user.text.toString(),
            "tel" to tel_user.text.toString(),
            "avatar" to imagenUri
        )

        db.collection("usuarios").document(idUsuario!!)
            .update(temp)
            .addOnSuccessListener {
                hideFragment(AccountSettingsFragment())
                Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show();
            }
            .addOnCompleteListener {
                this.recreate();
                super.onRestart();
            }
    }
}

