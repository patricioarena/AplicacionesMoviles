package com.example.jyc

import MyResources.Facade
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_comments.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        val intent  = intent
        postId = intent.getStringExtra("postId").toString()
        publicacionId = intent.getStringExtra("publicacionId").toString()
        userName = intent.getStringExtra("userName").toString()
        imagen = intent.getStringExtra("imagen").toString()
        idUsuario = intent.getStringExtra("idUsuario").toString()

        firebaseUser = FirebaseAuth.getInstance().currentUser

        Picasso.get().load(imagen).into(post_imagen_comments)

        post_comment.setOnClickListener{
            if(add_comment!!.text.toString().isEmpty()){
                Toast.makeText(this@CommentsActivity, "Escriba un comentario...", Toast.LENGTH_LONG).show()
            }
            else{
                addComment()
                getAllCommets()
            }
        }
    }

    private fun addComment(){
        facade = Facade()
        var fecha = facade.getDateTime()

        val commentMap = hashMapOf("fecha" to fecha.toString(),
                                   "idUsuario" to firebaseUser?.uid.toString(),
                                   "texto" to add_comment.text.toString())


        val comments = db.collection("comentarios")
        comments.add(commentMap).addOnSuccessListener { documentReference ->
            db.collection("publicaciones").document(postId)
                .update("comentarios", FieldValue.arrayUnion(documentReference.id))
                .addOnSuccessListener {
                    Toast.makeText(this, "Comentario Agregado", Toast.LENGTH_SHORT).show();
                    startActivity(Intent(this, HomeActivity::class.java))
                }
        }

        add_comment!!.text.clear()
    }

    private fun getAllCommets(){
        val publicacion = db.collection("publicaciones").document("iFYSz4whOzoJUU5QSkqq"
        ).get()
            .addOnSuccessListener { document ->
                println(document.data)
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: ")
            }
    }

}