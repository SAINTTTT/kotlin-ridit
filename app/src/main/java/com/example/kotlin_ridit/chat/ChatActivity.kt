package com.example.kotlin_ridit.chat

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_ridit.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {
    private lateinit var etChatInputText: EditText
    private lateinit var btnSendMessage: Button
    private lateinit var rvChatMessages: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var db: DatabaseReference
    var receiverUid: String? = null
    var senderUid: String? = null
    var receiverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        initRooms()
        initComponents()
        initUI()
    }

    private fun initRooms() {
        receiverUid = intent.getStringExtra("uid")
        senderUid = FirebaseAuth.getInstance().currentUser?.uid
        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid
        db = FirebaseDatabase.getInstance().getReference()
    }

    private fun initComponents() {
        etChatInputText = findViewById(R.id.etChatInputText)
        btnSendMessage = findViewById(R.id.btnSendMessage)
        rvChatMessages = findViewById(R.id.rvChatMessages)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)
        rvChatMessages.layoutManager = LinearLayoutManager(this)
        rvChatMessages.adapter = messageAdapter
    }

    private fun initUI() {
        btnSendMessage.setOnClickListener { sendMessage() }

        db.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapshot in snapshot.children) {
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    private fun sendMessage() {
        val message = etChatInputText.text.toString()
        if (message.isNotEmpty()) {
            val messageObject = Message(message, senderUid)

            db.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    db.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            Toast.makeText(
                baseContext, "Mensaje enviado",
                Toast.LENGTH_SHORT
            ).show()
            etChatInputText.text.clear()
        }
    }


}