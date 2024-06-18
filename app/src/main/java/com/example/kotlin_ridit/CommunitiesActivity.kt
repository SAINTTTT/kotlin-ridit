package com.example.kotlin_ridit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CommunitiesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var communityAdapter: CommunityAdapter
    private lateinit var communityList: MutableList<Community>
    private lateinit var btnCreateCommunity: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_communities)

        this.communityList = mutableListOf<Community>()
        val db = Firebase.firestore
        db.collection("communities")
            .whereArrayContains("members", FirebaseAuth.getInstance().currentUser?.email.toString())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val nombreComunidad = document.data["name"] as String
                    val miembros = document.data["members"] as ArrayList<*>
                    val cantidadDeMiembros = miembros.size
                    this.communityList.add(Community(nombreComunidad, cantidadDeMiembros))
                }

                runOnUiThread {
                    initComponents()
                    initUI()
                    recyclerView = findViewById(R.id.recyclerView)
                    recyclerView.layoutManager = LinearLayoutManager(this)
                    communityAdapter = CommunityAdapter(communityList)
                    recyclerView.adapter = communityAdapter
                }
            }

    }

    private fun initComponents() {
        btnCreateCommunity = findViewById(R.id.add_button)
    }

    private fun initUI() {
        btnCreateCommunity.setOnClickListener { navigateToCreateCommunity() }
    }

    private fun navigateToCreateCommunity() {
        val intent = Intent(this, CreateCommunityActivity::class.java)
        startActivity(intent)
    }
}


data class Community(
    val name: String,
    val members: Int
)

class CommunityAdapter(private val communityList: List<Community>) :
    RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder>() {

    class CommunityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val communityName: TextView = itemView.findViewById(R.id.community_name)
        val communityMembers: TextView = itemView.findViewById(R.id.community_members)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_community, parent, false)
        return CommunityViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CommunityViewHolder, position: Int) {
        val community = communityList[position]
        holder.communityName.text = community.name
        holder.communityMembers.text = "${community.members} miembros"
    }

    override fun getItemCount() = communityList.size
}
