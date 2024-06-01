package com.example.kotlin_ridit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_ridit.PostActivity.Companion.EXTRA_POST_ID
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivity : AppCompatActivity() {
    private lateinit var rvPosts: RecyclerView
    private lateinit var homePostsAdapter: HomePostsAdapter
    private lateinit var posts: MutableList<HomePost>
//    private val dummyHomePosts = listOf(
//        HomePost("Título 1", "Este es el contenido"),
//        HomePost("Título 4", "Quiero contarles algo"),
//        HomePost("Hace frío", "Es invierno y me quejo del frío"),
//        HomePost("Sube la luz", "Preparen las antorchas!!!")
//    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initComponent()
        initUI()
        CoroutineScope(Dispatchers.IO).launch {
            getHomePosts()
        }

    }

    private fun initComponent() {
        rvPosts = findViewById(R.id.rvHomePosts)
        posts = emptyList<HomePost>().toMutableList()
    }

    private fun initUI() {
        homePostsAdapter = HomePostsAdapter(posts) { navigateToPostItem(it) }
        rvPosts.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvPosts.adapter = homePostsAdapter
    }

    private fun navigateToPostItem(id: String) {
        val intent = Intent(this, PostActivity::class.java)
        intent.putExtra(EXTRA_POST_ID, id)
        startActivity(intent)
    }

    private fun getHomePosts() {
        val db = Firebase.firestore
        db.collection("posts")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    posts.add(
                        HomePost(
                            document.data["title"] as String,
                            document.data["content"] as String,
                            document.data["creator"] as String,
                            document.data["downvoteCount"].toString(),
                            document.data["upvoteCount"].toString(),
                            document.data["commentsCount"].toString(),
                            document.id as String
                        )
                    )
                    Log.d("FromFIRESTORE", "${document.id} => ${document.data}")
                }
                runOnUiThread {
                    homePostsAdapter.setData(posts)
                }
            }
            .addOnFailureListener { exception ->
                Log.d("FromFIRESTORE", "Error getting documents: ", exception)
            }
        Log.d("DENTRODEGETHOMEPOST", posts.toString())
    }
}

data class HomePost(
    val title: String,
    val content: String,
    val creator: String,
    val downvoteCount: String,
    val upvoteCount: String,
    val commentsCount: String,
    val id: String
)

class HomePostsAdapter(
    private val posts: List<HomePost>,
    private val onItemSelected: (String) -> Unit
) :
    RecyclerView.Adapter<HomePostsViewHolder>() {
    private val homeposts = mutableListOf<HomePost>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePostsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_home_post, parent, false)
        return HomePostsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return homeposts.size
    }

    override fun onBindViewHolder(holder: HomePostsViewHolder, position: Int) {
        if (homeposts.isNotEmpty()) {
            holder.render(homeposts[position], onItemSelected)
        }
    }

    fun setData(newPosts: List<HomePost>) {
        homeposts.clear()
        homeposts.addAll(newPosts)
        notifyDataSetChanged()
    }

    fun addData(newPost: HomePost) {
        homeposts.add(newPost)
        notifyDataSetChanged()
    }

}

class HomePostsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val tvPostTitle: TextView = view.findViewById(R.id.tvPostTitle)
    private val tvPostCreator: TextView = view.findViewById(R.id.tvPostCreator)
    private val tvPostContent: TextView = view.findViewById(R.id.tvPostContent)
    private val tvPostUpvoteCount: TextView = view.findViewById(R.id.tvPostUpvoteCount)
    private val tvPostDownvoteCount: TextView = view.findViewById(R.id.tvPostDownvoteCount)
    private val tvPostCommentsCount: TextView = view.findViewById(R.id.tvPostCommentsCount)

    private val root: View = view.rootView

    fun render(homePost: HomePost, onItemSelected: (String) -> Unit) {
        tvPostTitle.text = homePost.title
        tvPostContent.text = homePost.content
        tvPostCreator.text = homePost.creator
        tvPostUpvoteCount.text = homePost.upvoteCount
        tvPostDownvoteCount.text = homePost.downvoteCount
        tvPostCommentsCount.text = homePost.commentsCount
        root.setOnClickListener { onItemSelected(homePost.id) }
    }
}