package com.example.kotlin_ridit

import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PostHistoryActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private lateinit var postList: MutableList<Post>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_history)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        this.postList = mutableListOf<Post>()
        val db = Firebase.firestore
        db.collection("posts")
            .whereEqualTo("creator", FirebaseAuth.getInstance().currentUser?.email.toString())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val creator = document.data["creator"]
                    val title = document.data["title"] as String
                    val commentsCount = document.data["commentsCount"] as Long
                    val content = document.data["content"]
                    //val creationDate = document.data["creationDate"] as String
                    val downvoteCount = document.data["downvoteCount"] as Long
                    val upvoteCount = document.data["upvoteCount"] as Long
                    this.postList.add(Post(title,upvoteCount,downvoteCount,commentsCount))


                }
                println(this.postList)
                runOnUiThread {
                    postAdapter = PostAdapter(this.postList)
                    recyclerView.adapter = postAdapter
                }
            }




    }
}

data class Post(
    val title: String,
    val likes: Long,
    val dislikes: Long,
    val comments: Long,
    //val date: String
)

class PostAdapter(private val postList: List<Post>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_post, parent, false)
        return PostViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentPost = postList[position]
        holder.titleTextView.text = currentPost.title
        holder.likesTextView.text = currentPost.likes.toString()
        holder.dislikesTextView.text = currentPost.dislikes.toString()
        holder.commentsTextView.text = currentPost.comments.toString()
        // holder.dateTextView.text = currentPost.date
    }

    override fun getItemCount() = postList.size

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.tvPostTitle)
        val likesTextView: TextView = itemView.findViewById(R.id.tvPostUpvoteCount)
        val dislikesTextView: TextView = itemView.findViewById(R.id.tvPostDownvoteCount)
        val commentsTextView: TextView = itemView.findViewById(R.id.tvPostCommentsCount)
    }
}