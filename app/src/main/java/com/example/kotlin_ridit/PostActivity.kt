package com.example.kotlin_ridit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PostActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_POST_TITLE = "title"
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
        cv.findViewById<TextView>(R.id.tvPostTitle).text = intent.getStringExtra(EXTRA_POST_TITLE).orEmpty()
        cv.findViewById<TextView>(R.id.tvPostCreator).text = "Usuario X"
        cv.findViewById<TextView>(R.id.tvPostContent).text = "El contenido del post es este"
        cv.findViewById<TextView>(R.id.tvPostUpvoteCount).text = "55"
        cv.findViewById<TextView>(R.id.tvPostDownvoteCount).text = "10"
        cv.findViewById<TextView>(R.id.tvPostCommentsCount).text = "4"
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