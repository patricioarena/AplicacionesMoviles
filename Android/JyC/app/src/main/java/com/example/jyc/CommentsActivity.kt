package com.example.jyc

import MyResources.Facade
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_comments.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_publication.*
import kotlinx.android.synthetic.main.card_post.view.*

class CommentsActivity : AppCompatActivity() {
    private var postId = ""
    private var publicacionId = ""
    private var userName = ""
    private var imagen = ""
    private var idUsuario = ""
    private var firebaseUser: FirebaseUser? = null
    private var db = FirebaseFirestore.getInstance()

    private lateinit var facade: Facade

    var comentarios = mutableListOf<Comment>()
    var comentarios2 = mutableListOf<Comment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        val intent = intent
        postId = intent.getStringExtra("postId").toString()
        publicacionId = intent.getStringExtra("publicacionId").toString()
        userName = intent.getStringExtra("userName").toString()
        imagen = intent.getStringExtra("imagen").toString()
        idUsuario = intent.getStringExtra("idUsuario").toString()
        var listaIdcomentarios = intent.getStringArrayListExtra("listaIdcomentarios")

        //println(listaIdcomentarios)

        firebaseUser = FirebaseAuth.getInstance().currentUser

        Picasso.get().load(imagen).into(post_imagen_comments)

        getAllCommets(listaIdcomentarios)

        post_comment.setOnClickListener {
            if (add_comment!!.text.toString().isEmpty()) {
                Toast.makeText(this@CommentsActivity, "Escriba un comentario...", Toast.LENGTH_LONG)
                    .show()
            } else {
                addComment()
            }
        }
    }

    private fun addComment() {
        facade = Facade()
        var fecha = facade.getDateTime()

        val commentMap = hashMapOf(
            "fecha" to fecha.toString(),
            "idUsuario" to firebaseUser?.uid.toString(),
            "texto" to add_comment.text.toString()
        )



        val comments = db.collection("comentarios")
        comments.add(commentMap).addOnSuccessListener { documentReference ->
            db.collection("publicaciones").document(postId)
                .update("comentarios", FieldValue.arrayUnion(documentReference.id))
                .addOnSuccessListener {
                    Toast.makeText(this, "Comentario Agregado", Toast.LENGTH_SHORT).show();
//                    startActivity(Intent(this, HomeActivity::class.java))

                    //Creaamos un nuevo objeto comnet para guardar los datos del mensaje que se acaba de enviar al servidor
                    var comment = Comment()


                    comment.date = commentMap["fecha"].toString()
                    comment.idUsuario = commentMap["idUsuario"].toString()
                    comment.texto = commentMap["texto"].toString()

                    //falta recuperar el nombre de usuario actual
                    comment.userName = "Patricio E. Arena"

                    // almacenamos el comentario en la lista de comentarios que ya teniamos
                    comentarios2.add(comment)

                    //limpiamos el recycler para volver a dibujar los comentarios incluyendo el comentario nuevo
                    // de este modo no tenemos que ir al servidor a recuperar el comentario que realizo el usuario
                    recycler_view_comment.getRecycledViewPool().clear();
                    recycler_view_comment.layoutManager = LinearLayoutManager(this)
                    recycler_view_comment.adapter = CommentsAdapter(this, comentarios2)
                    //desplazamos la vista hacia la parte inferior donde aparecera el nuevo comentario
                    (recycler_view_comment.layoutManager as LinearLayoutManager).scrollToPosition(comentarios2.count() -1)
                }
        }

        add_comment!!.text.clear()
    }

    // .orderBy("fecha", Query.Direction.DESCENDING)
    private fun getAllCommets(listaIdcomentarios: ArrayList<String>?) {

        comentarios.clear()
        comentarios2.clear()

        if (listaIdcomentarios != null) {

            for (idComentario in listaIdcomentarios) {
                var comment = Comment()
                db.collection("comentarios").document(idComentario).get()
                    .addOnSuccessListener { result ->

                        val document = result
                        comment.idUsuario = document?.data?.get("idUsuario").toString()
                        comment.date = document?.data?.get("fecha").toString()
                        comment.texto = document?.data?.get("texto").toString()
                        comentarios.add(comment)
                    }
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            db.collection("usuarios")
                                .document(comment.idUsuario.toString()).get()
                                .addOnSuccessListener { result ->
                                    val document = result
                                    comment.userName = document?.data?.get("nombre").toString() + " " + document?.data?.get("apellido").toString()
                                    comentarios2.add(comment)
                                }.addOnCompleteListener{
                                    var temp = comentarios2.toList();
                                    if (temp != null) {
                                        recycler_view_comment.layoutManager = LinearLayoutManager(this)
                                        recycler_view_comment.adapter = CommentsAdapter(this, temp)
                                    }
                                }



                        }
                    }
            }


        }
    }
}

