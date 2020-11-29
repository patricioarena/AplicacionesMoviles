package com.example.jyc

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_post.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


open class PostAdapter(private val activity: Activity, private val dataset: List<Post>, private val itemClickListener: OnPublicacionesClickListener) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    interface OnPublicacionesClickListener{
        fun onImageClick(image: String?)
        fun onItemClick(idUsuario: String?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicacionesViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.card_post, parent, false)
        return PublicacionesViewHolder(layout)
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when(holder){
            is PublicacionesViewHolder -> holder.bind(dataset[position],position)
            else -> throw IllegalAccessException("Se olvido de pasar el viewholder en el bind")
        }

    }

    inner class PublicacionesViewHolder(val layout: View) : BaseViewHolder<Post>(layout){
        override fun bind(item: Post, position: Int) {

            itemView.setOnClickListener{itemClickListener.onItemClick(item.idUsuario)}
            itemView.image_tv.setOnClickListener{itemClickListener.onImageClick(item.image)}
//            var post = dataset[position]
            var likes = item.likes
            var usuarioActual = auth.currentUser

            var liked = likes?.contains(usuarioActual?.uid.toString())

            if (liked == null) {
                liked = false
            }

            if (item.nombreEvento == "null"){
                layout.CardTitle.setVisibility(View.GONE)
                layout.buttonMore.setVisibility(View.GONE)
            }

            layout.CardTitle.text = item.nombreEvento
            layout.username_tv.text = item.userName
            layout.post_tv.text = item.post
            Picasso.get().load(item.image).into(layout.image_tv)
            layout.likesCount_tv.text = "${item.cantidadDeLikes} likes"
            layout.fecha_tv.text = item.date
            layout.commentsCount_tv.text = "${item.cantidadDeComentarios} comentarios"

            setColor(liked, layout.like_btn)

            layout.like_btn.setOnClickListener {

                setColor(liked, layout.like_btn)

                if (liked?.equals(true)!!) {
                    removeUserDb(item.uid)
                } else {
                    insertUerDb(item.uid)
                }
            }

            layout.share_btn.setOnClickListener {
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, item.post)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                activity.startActivity(shareIntent)
            }

            layout.comment_btn.setOnClickListener{
                val commentIntent = Intent(activity,CommentsActivity::class.java)
                commentIntent.putExtra("postId", item.uid )
                commentIntent.putExtra("publicacionId", item.post)
                commentIntent.putExtra("userName", item.userName)
                commentIntent.putExtra("imagen", item.image)
                commentIntent.putExtra("idUsuario", item.idUsuario)
                //paso los id de comentarios que tiene una publicacion
                commentIntent.putStringArrayListExtra("listaIdcomentarios", item.listaIdcomentarios!!)
                activity.startActivity(commentIntent)
            }


        }

    }

    private fun setColor(liked: Boolean?, likedButton: Button) {
        if (liked!!) likedButton.setTextColor(ContextCompat.getColor(activity, R.color.purple_500))
        else likedButton.setTextColor(Color.WHITE)
    }

    private fun removeUserDb(uid: String?) {
        if (uid != null) {
            val doc = db.collection("publicaciones").document(uid)
            db.runTransaction {
                it.update(doc, "likes", FieldValue.arrayRemove(auth.uid))

                null
            }
        }

    }

    private fun insertUerDb(uid: String?) {
        if (uid != null) {
            val doc = db.collection("publicaciones").document(uid)
            db.runTransaction {
                it.update(doc, "likes", FieldValue.arrayUnion(auth.uid))

                null
            }
        }

    }

}