package com.example.kotlin_ridit

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_ridit.PostActivity.Companion.EXTRA_POST_TITLE

class HomeActivity : AppCompatActivity() {
    private lateinit var rvPosts: RecyclerView
    private lateinit var homePostsAdapter: HomePostsAdapter
    private val dummyHomePosts = listOf(
        HomePost("Título 1", "Este es el contenido"),
        HomePost("Título 4", "Quiero contarles algo"),
        HomePost("Hace frío", "Es invierno y me quejo del frío"),
        HomePost("Sube la luz", "Preparen las antorchas!!!")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        initComponent()
        initUI()

    }

    private fun initComponent() {
        rvPosts = findViewById(R.id.rvHomePosts)
    }

    private fun initUI() {
        homePostsAdapter = HomePostsAdapter(dummyHomePosts) { navigateToPostItem(it) }
        rvPosts.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvPosts.adapter = homePostsAdapter
    }

    private fun navigateToPostItem(title: String) {
        val intent = Intent(this, PostActivity::class.java)
        intent.putExtra(EXTRA_POST_TITLE, title)
        startActivity(intent)
    }
}

data class HomePost(val title: String, val content: String)

class HomePostsAdapter(
    private val posts: List<HomePost>,
    private val onItemSelected: (String) -> Unit
) :
    RecyclerView.Adapter<HomePostsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePostsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_home_post, parent, false)
        return HomePostsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: HomePostsViewHolder, position: Int) {
        holder.render(posts[position], onItemSelected)
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
        tvPostCreator.text = "Usario X"
        tvPostUpvoteCount.text = (Math.random() * 100).toInt().toString()
        tvPostDownvoteCount.text = (Math.random() * 100).toInt().toString()
        tvPostCommentsCount.text = (Math.random() * 100).toInt().toString()
        root.setOnClickListener { onItemSelected(homePost.title) }
    }
}