package com.example.projetfinaljeu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_game_like.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameLikeFragment : Fragment() {
    private val listGame: GameLikeFragmentArgs by navArgs()
    lateinit var rv: RecyclerView
    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance().reference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_like, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar_home)

        progressBar.visibility=View.VISIBLE

        list_game_like_recyclerview.visibility=View.GONE

        GlobalScope.launch(Dispatchers.Default) {

            withContext(Dispatchers.Main) {
                getLikedGame(view)


            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    private fun getLikedGame(view: View) {
        val likedVideos = mutableListOf<String>()

        val imgL = view.findViewById<ImageView>(R.id.imageStartView)
        val textl = view.findViewById<TextView>(R.id.check_start_text_view)
        val constraint = view.findViewById<ConstraintLayout>(R.id.constraint_id)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar_home)

        // Get reference to the "likes" child in Firebase
        val likesRef = database.child("game").child("likes" )

        // Get a listener for the "likes" child
        likesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (likeSnapshot in dataSnapshot.children) {
                    val like = likeSnapshot.getValue(Like::class.java)

                    if (like != null && like.userId == listGame.userArgs.uid)
                        likedVideos.add(like.appId)

                }
                progressBar.visibility=View.GONE
                val games: MutableList<Game> = mutableListOf()


                likedVideos.forEach { el->
                    listGame.gameDataArgs.forEach{
                        if( it.appid.toString() == el) {
                           games.add(it!!)
                       }
                    }
                }

                if(games.isNotEmpty()) {
                    if(constraint != null)
                        constraint.visibility=View.GONE
                    if(list_game_like_recyclerview != null){
                        list_game_like_recyclerview.visibility=View.VISIBLE

                        rv = list_game_like_recyclerview
                        //scroller vers le bas
                        rv.layoutManager = LinearLayoutManager(context)
                        rv.adapter = GamesAdapter(games.toList(), listener, getString(R.string.item_price))
                    }

                }else{
                    if(constraint != null)
                        constraint.visibility=View.VISIBLE
                    if(imgL != null)
                        imgL.visibility=View.GONE
                    if(textl != null)
                        textl.visibility=View.GONE

                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }
    private val listener = GamesAdapter.OnClickListener { game ->
        // Add action to navigate
        findNavController().navigate(
            GameLikeFragmentDirections.actionGameLikeFragmentToGameDetailFragment(game, listGame.userArgs)
        )

    }
}