package com.example.kotlin_ridit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.sql.Date
import java.text.SimpleDateFormat

class CommentsHistoryActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var commentList: MutableList<Comment>
    private lateinit var titlePost : StringBuilder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments_history)

        recyclerView = findViewById(R.id.recyclerViewComments)
        recyclerView.layoutManager = LinearLayoutManager(this)

        this.commentList = mutableListOf<Comment>()
        this.titlePost = StringBuilder("Los String son inmutables")

        val db = Firebase.firestore
        db.collection("comments")
            .whereEqualTo("creator", FirebaseAuth.getInstance().currentUser?.email.toString())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val content = document.data["content"] as String
                    val creationDate = document.data["creationDate"] as Timestamp
                    val titleId = document.data["commentsTo"].toString().substring(7)

                    fetchPostTitle(titleId) { title ->
                        this.titlePost.append(title)

                    }
                    this.commentList.add(Comment(
                        this.titlePost.toString(),content,
                        SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(creationDate.toDate())))

                }

                runOnUiThread {
                    commentAdapter = CommentAdapter(this.commentList)
                    recyclerView.adapter = commentAdapter
                }
            }
    }

    private fun fetchPostTitle(titleId: String, callback: (String) -> Unit) {
        Firebase.firestore.collection("posts").document(titleId).get()
            .addOnSuccessListener { documentPost ->
                val dataPosts = documentPost.data
                val returnTitle = dataPosts?.get("title")?.toString().toString()
                callback(returnTitle)

            }
    }


}


data class Comment(
    val title: String,
    val body: String,
    val date: String
)

class CommentAdapter(private val commentList: List<Comment>) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_comment_history, parent, false)
        return CommentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val currentComment = commentList[position]
        holder.titleTextView.text = currentComment.title
        holder.bodyTextView.text = currentComment.body
        holder.dateTextView.text = currentComment.date
    }

    override fun getItemCount() = commentList.size

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.tvCommentTitle)
        val bodyTextView: TextView = itemView.findViewById(R.id.tvCommentBody)
        val dateTextView: TextView = itemView.findViewById(R.id.tvCommentDate)
    }
}
