package com.example.jyc

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_post.view.*
import kotlinx.android.synthetic.main.comment.view.*

class CommentsAdapter(private val activity: Activity, private val dataset: List<Comment>) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComentariosViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.comment, parent, false)
        return ComentariosViewHolder(layout)
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when(holder){
            is ComentariosViewHolder -> holder.bind(dataset[position],position)
            else -> throw IllegalAccessException("Se olvido de pasar el viewholder en el bind")
        }
    }

    inner class ComentariosViewHolder(val layout: View) : BaseViewHolder<Comment>(layout){
        override fun bind(item: Comment, position: Int) {

            layout.user_name_comment.text = item.userName
            layout.comment_comment.text = item.texto
            layout.date_comment.text = item.date
        }

    }


}