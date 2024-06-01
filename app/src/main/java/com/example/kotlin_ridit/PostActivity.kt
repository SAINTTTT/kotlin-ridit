package com.example.kotlin_ridit

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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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

    private val dummyComments = listOf(
        PostComment("usuario Y", "ja ja"),
        PostComment("usuario Z", "ve a trabajar"),
        PostComment("usuario R", "No lo sonieee")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        initComponent()
        initUI()
    }

    private fun initComponent() {
        cvPost = findViewById(R.id.cvPost)
        initDummyPost(cvPost)
        rvItemPostComments = findViewById(R.id.rvItemPostComments)
    }

    private fun initUI() {
        commentsAdapter = PostCommentsAdapter(dummyComments)
        rvItemPostComments.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvItemPostComments.adapter = commentsAdapter
    }

    private fun initDummyPost(cv: CardView) {
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
        //traer comentario
        val db = Firebase.firestore
        db.collection("posts").document(intent.getStringExtra(EXTRA_POST_ID).orEmpty())
            .get().addOnSuccessListener { document ->
                if (document != null) {

                    Log.d("FIRESTORE-GET-POST", "{$document.id} => {$document.data}")
                } else {
                    Log.d("FIRESTORE-GET-POST", "NO EXISTE EL DOCUMENTO")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("FIRESTORE-GET-POST", exception.toString())
            }
        ////

    }
}

data class PostComment(val user: String, val content: String)

class PostCommentsAdapter(private val comments: List<PostComment>) :
    RecyclerView.Adapter<PostCommentsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostCommentsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_post_comment, parent, false)
        return PostCommentsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(holder: PostCommentsViewHolder, position: Int) {
        holder.render(comments[position])
    }
}

class PostCommentsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val tvCommentCreator: TextView = view.findViewById(R.id.tvCommentCreator)
    private val tvCommentContent: TextView = view.findViewById(R.id.tvCommentContent)

    fun render(comment: PostComment) {
        tvCommentCreator.text = comment.user
        tvCommentContent.text = comment.content
    }

}