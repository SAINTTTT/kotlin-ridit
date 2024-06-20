package com.example.kotlin_ridit

import android.content.Context
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
import com.example.kotlin_ridit.chat.ChatActivity
import com.example.kotlin_ridit.chat.MessageAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.getField
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.google.rpc.context.AttributeContext.Auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.reflect.typeOf

class MessagesActivity : AppCompatActivity() {

    private lateinit var rvMessages: RecyclerView
    private lateinit var chatWithUserItemAdapter: ChatWithUserItemAdapter
    private lateinit var chatsWith: ArrayList<ChatWithUserItem>
    private lateinit var cvNoMessages: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)
        initComponents()
        initUI()
        CoroutineScope(Dispatchers.IO).launch {
            getChats()
        }
    }

    private fun initComponents() {
        rvMessages = findViewById(R.id.rvMessages)
        chatsWith = ArrayList()
        cvNoMessages = findViewById(R.id.cvNoMessages)
    }

    private fun initUI() {
        chatWithUserItemAdapter =
            ChatWithUserItemAdapter(this, chatsWith) { item -> navigateToChatActivity(item) }
        rvMessages.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvMessages.adapter = chatWithUserItemAdapter
    }

    private fun getChats() {
        val user = FirebaseAuth.getInstance().currentUser
        Firebase.firestore.collection("users")
            .whereEqualTo(FieldPath.documentId(), user?.email)
            .get().addOnSuccessListener { snapshot ->
                for (document in snapshot) {
                    if (document.data["chatsWith"] != null) {
                        val listOfChats = document.data["chatsWith"] as ArrayList<*>
                        for (chat in listOfChats) {
                            val chatItem = ChatWithUserItem(
                                (chat as HashMap<*, *>)["name"].toString(),
                                (chat as HashMap<*, *>)["uid"].toString()
                            )
                            chatsWith.add(chatItem)
                        }

                    }

                }

            }.addOnSuccessListener {
                runOnUiThread() {
                    if (chatsWith.isEmpty()) {
                        cvNoMessages.visibility = View.VISIBLE
                    } else {
                        cvNoMessages.visibility = View.GONE
                    }
                    chatWithUserItemAdapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener {
                runOnUiThread() {
                    chatsWith = emptyList<ChatWithUserItem>() as ArrayList<ChatWithUserItem>
                    cvNoMessages.visibility = View.VISIBLE
                }

            }
    }

    private fun navigateToChatActivity(chat: ChatWithUserItem) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("uid", chat.uid)
        startActivity(intent)
    }
}

data class ChatWithUserItem(val name: String, val uid: String)

class ChatWithUserItemAdapter(
    val context: Context,
    val chatWithUserList: ArrayList<ChatWithUserItem>,
    val onChatSelected: (ChatWithUserItem) -> Unit
) : RecyclerView.Adapter<ChatWithUserItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatWithUserItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false)
        return ChatWithUserItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return chatWithUserList.size
    }

    override fun onBindViewHolder(holder: ChatWithUserItemViewHolder, position: Int) {
        holder.render(chatWithUserList[position], onChatSelected)
    }

}

class ChatWithUserItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvChatWithUser: TextView = view.findViewById(R.id.tvChatWithUser)
    val root = view.rootView
    fun render(chatWithUserItem: ChatWithUserItem, onChatSelected: (ChatWithUserItem) -> Unit) {
        tvChatWithUser.text = chatWithUserItem.name
        root.setOnClickListener { onChatSelected(chatWithUserItem) }
    }
}
