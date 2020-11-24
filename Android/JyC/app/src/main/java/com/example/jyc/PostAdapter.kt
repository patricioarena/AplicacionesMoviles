package com.example.jyc

import MyResources.Facade
import android.app.Activity
import android.app.MediaRouteButton
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
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class PostAdapter(private val activity: Activity , private val dataset: List<Post>) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()


    class ViewHolder (val layout: View) : RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.card_post, parent, false)

        return ViewHolder(layout)
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = dataset[position]
        val likes = post.likes!!.toMutableList()
        var liked = likes.contains(auth.uid)
    //    service = Facade()

        holder.layout.username_tv.text = post.userName
        holder.layout.post_tv.text = post.post
        Picasso.get().load(post.image).into(holder.layout.image_tv)
        holder.layout.likesCount_tv.text = "${post.cantidadDeLikes} likes"

    //    var date = service.getDateTime()

//        DateFormat dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//        Date date = new Date();
//        String strDate = dateFormat.format(post.date).toString();

 //       val date = SimpleDateFormat("dd/M/yyyy hh:mm a")

        holder.layout.fecha_tv.text = post.date

        setColor(liked, holder.layout.like_btn)

        holder.layout.like_btn.setOnClickListener {
            val likesX = post.likes!!.toMutableList()
            var likedX = likesX.contains(auth.uid)
            println(likedX)
    //        liked = !liked
            setColor(likedX, holder.layout.like_btn)

    //        if(liked) likes.add(auth.uid!!)
    //        else likes.remove(auth.uid)

            if (likedX == true){
                println("aaaaaaaaaaaaaaaaaaaaaaaaaa")
                val doc = db.collection("publicaciones").document(post.uid!!)

                db.runTransaction{
                    it.update(doc, "likes", FieldValue.arrayRemove(auth.uid))

                    null
                }.addOnSuccessListener {
                    likesX.remove(auth.uid)
                }
            }else{
                val doc = db.collection("publicaciones").document(post.uid!!)

                db.runTransaction{
                    it.update(doc, "likes", FieldValue.arrayUnion(auth.uid))

                    null
                }.addOnSuccessListener {
                    likesX.add(auth.uid!!)
                }
            }
        }

        holder.layout.share_btn.setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, post.post)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            activity.startActivity(shareIntent)
        }

    }

    private fun setColor(liked: Boolean, likedButton: Button){
        if (liked) likedButton.setTextColor(ContextCompat.getColor(activity, R.color.purple_500))
        else likedButton.setTextColor(Color.WHITE)
    }
}