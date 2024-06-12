package com.example.kotlin_ridit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_ridit.CommunityHomeActivity.Companion.EXTRA_COMMUNITY_NAME
import com.example.kotlin_ridit.PostActivity.Companion.EXTRA_POST_COMMENTS_COUNT
import com.example.kotlin_ridit.PostActivity.Companion.EXTRA_POST_CONTENT
import com.example.kotlin_ridit.PostActivity.Companion.EXTRA_POST_CREATOR
import com.example.kotlin_ridit.PostActivity.Companion.EXTRA_POST_DOWNVOTES_COUNT
import com.example.kotlin_ridit.PostActivity.Companion.EXTRA_POST_ID
import com.example.kotlin_ridit.PostActivity.Companion.EXTRA_POST_POSTED_ON
import com.example.kotlin_ridit.PostActivity.Companion.EXTRA_POST_TITLE
import com.example.kotlin_ridit.PostActivity.Companion.EXTRA_POST_UPVOTES_COUNT
import com.example.kotlin_ridit.PublicProfileActivity.Companion.EXTRA_USER_NAME
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    private lateinit var rvPosts: RecyclerView
    private lateinit var homePostsAdapter: HomePostsAdapter
    private lateinit var posts: MutableList<HomePost>
    private lateinit var fabCreatePost: FloatingActionButton
    private lateinit var btnProfile: Button

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
        fabCreatePost = findViewById(R.id.fabCreatePost)
        btnProfile = findViewById(R.id.btnProfile)
    }

    private fun initUI() {
        homePostsAdapter =
            HomePostsAdapter(
                posts,
                { item -> navigateToPostItem(item) },
                { communityName: String -> run { navigateToCommunityHome(communityName)} },
                { postCreator: String -> run { navigateToPostCreatorPublicProfile(postCreator) } }) // esto no sé por qué funciona así
        rvPosts.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        rvPosts.adapter = homePostsAdapter
        fabCreatePost.setOnClickListener { navigateToCreatePost() }
        btnProfile.setOnClickListener { navigateToProfile() }
    }

    private fun navigateToProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToPostItem(post: HomePost) {
        val intent = Intent(this, PostActivity::class.java)
        intent.putExtra(EXTRA_POST_ID, post.id)
        intent.putExtra(EXTRA_POST_TITLE, post.title)
        intent.putExtra(EXTRA_POST_CONTENT, post.content)
        intent.putExtra(EXTRA_POST_CREATOR, post.creator)
        intent.putExtra(EXTRA_POST_UPVOTES_COUNT, post.upvoteCount)
        intent.putExtra(EXTRA_POST_DOWNVOTES_COUNT, post.downvoteCount)
        intent.putExtra(EXTRA_POST_COMMENTS_COUNT, post.commentsCount)
        intent.putExtra(EXTRA_POST_POSTED_ON, post.postedOn)
        startActivity(intent)
    }

    private fun navigateToCreatePost() {
        Log.i("FAB", "FAB Click")
        val intent = Intent(this, CreatePostActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToCommunityHome(communityName: String) {
        val intentToCommunity = Intent(this, CommunityHomeActivity::class.java)
        intentToCommunity.putExtra(EXTRA_COMMUNITY_NAME, communityName)
        startActivity(intentToCommunity)
    }

    private fun navigateToPostCreatorPublicProfile(postCreator: String) {
        val intent = Intent(this, PublicProfileActivity::class.java)
        intent.putExtra(EXTRA_USER_NAME, postCreator)
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
                            document.id as String,
                            document.data["postedOn"]?.toString().orEmpty()
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

    override fun onResume() {
        super.onResume()
        // Hay que ver de mejorar esto para que sea más eficiente
        // ya que cada vez que vuelve se llama otra vez a la BBDD
        posts.clear()
        getHomePosts()
    }
}

data class HomePost(
    val title: String,
    val content: String,
    val creator: String,
    val downvoteCount: String,
    val upvoteCount: String,
    val commentsCount: String,
    val id: String,
    val postedOn: String
)

class HomePostsAdapter(
    private val posts: List<HomePost>,
    private val onItemSelected: (HomePost) -> Unit,
    private val onCommunityClick: (String) -> Unit,
    private val onPostCreatorClick: (String) -> Unit
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
            holder.render(homeposts[position], onItemSelected, onCommunityClick, onPostCreatorClick)
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
    private val tvPostCommunity: TextView = view.findViewById(R.id.tvPostCommunity)

    private val root: View = view.rootView

    fun render(
        homePost: HomePost,
        onItemSelected: (HomePost) -> Unit,
        onCommunityClick: (String) -> Unit,
        onPostCreatorClick: (String) -> Unit
    ) {
        tvPostTitle.text = homePost.title
        tvPostContent.text = homePost.content
        tvPostCreator.text = homePost.creator
        tvPostUpvoteCount.text = homePost.upvoteCount
        tvPostDownvoteCount.text = homePost.downvoteCount
        tvPostCommentsCount.text = homePost.commentsCount
        tvPostCommunity.text = homePost.postedOn // provisorio
        root.setOnClickListener { onItemSelected(homePost) }
        tvPostCommunity.setOnClickListener { onCommunityClick(homePost.postedOn) }
        tvPostCreator.setOnClickListener { onPostCreatorClick(homePost.creator) }
    }
}