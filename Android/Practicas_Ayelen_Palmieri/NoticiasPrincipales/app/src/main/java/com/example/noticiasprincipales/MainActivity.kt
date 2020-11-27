package com.example.noticiasprincipales

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noticiasprincipales.models.Post
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
//    private lateinit var myadapter: RecyclerView.Adapter<*>
//    private lateinit var manager: RecyclerView.LayoutManager

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        db.collection("posts").addSnapshotListener { value, error ->
            val posts = value!!.toObjects(Post::class.java)

            posts.forEachIndexed { index, post ->
                post.uid = value.documents[index].id
            }

            recyclerView.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = PostsAdapter(this@MainActivity, posts)
            }
        }


//        manager = LinearLayoutManager(this)
//        myadapter = PostsAdapter(post, this)
//        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
//            setHasFixedSize(true)
//            layoutManager = manager
//            adapter = myadapter
        }

    }


}