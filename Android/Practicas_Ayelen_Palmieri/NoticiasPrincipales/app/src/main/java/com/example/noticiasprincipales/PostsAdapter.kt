package com.example.noticiasprincipales


import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.noticiasprincipales.models.Post
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

class PostsAdapter(private val activity: Activity, private val dataset: List<Post>) : RecyclerView.Adapter<PostsAdapter.ViewHolder>(){

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    class ViewHolder(val layout: View) : RecyclerView.ViewHolder(layout){
//        val username: TextView = itemView.findViewById(R.id.username_tv)
//        val text: TextView = itemView.findViewById(R.id.text_description_tv)
//        val photo: ImageView = itemView.findViewById(R.id.photo_img)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsAdapter.ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.post_row, parent, false)
        return ViewHolder(layout)
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = dataset[position]
        val likes = post.likes!!.toMutableList()
        var liked = likes.contains(auth.uid)

        holder.layout.likes_tv.text = "${likes.size} likes"
        holder.layout.username_tv.text = post.username
        holder.layout.text_description_tv.text = post.post

        val fecha = SimpleDateFormat("dd/M/yyyy hh:mm a")

        holder.layout.fecha_post_tv.text = fecha.format(post.date)
        setColor(liked,holder.layout.like_btn)

        holder.layout.like_btn.setOnClickListener{
            liked = !liked
            setColor(liked,holder.layout.like_btn)

            if (liked) likes.add(auth.uid!!)
            else likes.remove(auth.uid)

            val doc = db.collection("posts").document(post.uid!!)

            db.runTransaction{
                it.update_(doc,"likes", likes)

                null
            }
        }

        Picasso.get().load(post.photo).into(holder.layout.photo_img)

        holder.layout.share_btn.setOnClickListener{
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, post.post)
                type = "text/plain"
            }

            val shareIntent = Intent.createChoose(sendIntent, null)
            activity.startActivity(shareIntent)
        }
    }

    private fun setColor(liked: Boolean, likeButton: Button) {
        if (liked) likeButton.setTextColor(ContextCompat.getColor(activity, R.color.design_default_color_primary))
        else likeButton.setTextColor(Color.BLACK)
    }

}