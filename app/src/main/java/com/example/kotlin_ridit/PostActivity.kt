package com.example.kotlin_ridit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_ridit.CreateCommentActivity.Companion.EXTRA_COMMENT_POST_ID
import com.example.kotlin_ridit.CreateCommentActivity.Companion.EXTRA_COMMENT_POST_TITLE
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_POST_ID = "postId"
        const val EXTRA_POST_TITLE = "postTitle"
        const val EXTRA_POST_CONTENT = "postContent"
        const val EXTRA_POST_CREATOR = "postCreator"
        const val EXTRA_POST_UPVOTES_COUNT = "postUpvotes"
        const val EXTRA_POST_DOWNVOTES_COUNT = "postDownvotes"
        const val EXTRA_POST_COMMENTS_COUNT = "postCommnent"
    }

    private lateinit var cvPost: CardView
    private lateinit var rvItemPostComments: RecyclerView
    private lateinit var commentsAdapter: PostCommentsAdapter
    private lateinit var comments: MutableList<PostComment>
    private lateinit var tvCommunity: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        initComponent()
        initUI()
        CoroutineScope(Dispatchers.IO).launch {
            getPostComments()
        }
    }

    private fun initComponent() {
        cvPost = findViewById(R.id.cvPost)
        initPost(cvPost)
        comments = emptyList<PostComment>().toMutableList()
        rvItemPostComments = findViewById(R.id.rvItemPostComments)
        tvCommunity = findViewById(R.id.tvCommunity)
    }

    private fun initUI() {
        commentsAdapter = PostCommentsAdapter(emptyList())
        rvItemPostComments.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvItemPostComments.adapter = commentsAdapter
        tvCommunity.setOnClickListener{ navigateToCommunityHome() }
    }

    private fun initPost(cv: CardView) {
        cv.findViewById<TextView>(R.id.tvPostTitle).text = intent.getStringExtra(EXTRA_POST_TITLE)
        cv.findViewById<TextView>(R.id.tvPostCreator).text =
            intent.getStringExtra(EXTRA_POST_CREATOR)
        cv.findViewById<TextView>(R.id.tvPostContent).text =
            intent.getStringExtra(EXTRA_POST_CONTENT)
        cv.findViewById<TextView>(R.id.tvPostUpvoteCount).text =
            intent.getStringExtra(EXTRA_POST_UPVOTES_COUNT)
        cv.findViewById<TextView>(R.id.tvPostDownvoteCount).text =
            intent.getStringExtra(EXTRA_POST_DOWNVOTES_COUNT)
        cv.findViewById<TextView>(R.id.tvPostCommentsCount).text =
            intent.getStringExtra(EXTRA_POST_COMMENTS_COUNT)
        cv.findViewById<TextView>(R.id.tvPostCommentsIcon)
            .setOnClickListener { navigateToCommentPost() }
    }

    private fun getPostComments() {
        val db = Firebase.firestore
        db.collection("comments")
            .whereEqualTo("commentsTo", "/posts/${intent.getStringExtra(EXTRA_POST_ID)}")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val creator = document.data["creator"]
                    val content = document.data["content"] as String

                    comments.add(PostComment(creator?.toString() ?: "usuario desconocido", content))

                    Log.d("FIRESTORE-GET-COMMENTS", "${document.id} => ${document.data}")
                }
                runOnUiThread {
                    commentsAdapter.setData(comments)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("FIRESTORE-GET-COMMENTS", "Error getting documents: ", exception)
            }
    }

    private fun navigateToCommentPost() {
        val intentToComment = Intent(this, CreateCommentActivity::class.java)
        intentToComment.putExtra(EXTRA_COMMENT_POST_TITLE, intent.getStringExtra(EXTRA_POST_TITLE))
        intentToComment.putExtra(EXTRA_COMMENT_POST_ID, intent.getStringExtra(EXTRA_POST_ID))
        startActivity(intentToComment)
    }

    private fun navigateToCommunityHome() {
        val intent = Intent(this, CommunityHomeActivity::class.java)
        startActivity(intent)
    }
}

data class PostComment(val creator: String, val content: String)
class PostCommentsAdapter(private val comments: List<PostComment>) :
    RecyclerView.Adapter<PostCommentsViewHolder>() {

    private val postComments = mutableListOf<PostComment>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostCommentsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_post_comment, parent, false)
        return PostCommentsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postComments.size
    }

    override fun onBindViewHolder(holder: PostCommentsViewHolder, position: Int) {
        holder.render(postComments[position])
    }

    fun setData(comments: MutableList<PostComment>) {
        postComments.clear()
        postComments.addAll(comments)
        notifyDataSetChanged()
    }
}

class PostCommentsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val tvCommentCreator: TextView = view.findViewById(R.id.tvCommentCreator)
    private val tvCommentContent: TextView = view.findViewById(R.id.tvCommentContent)

    fun render(comment: PostComment) {
        tvCommentCreator.text = comment.creator
        tvCommentContent.text = comment.content
    }

}