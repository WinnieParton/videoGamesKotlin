package com.example.projetfinaljeu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
import kotlinx.android.synthetic.main.fragment_game_wish.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameWishFragment : Fragment() {
    private val listGame: GameWishFragmentArgs by navArgs()
    lateinit var rv: RecyclerView
    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance().reference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_wish, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actionbar = (activity as AppCompatActivity).supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setHomeAsUpIndicator(R.drawable.close)


        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar_home)

        progressBar.visibility=View.VISIBLE

        list_game_wish_recyclerview.visibility=View.GONE
        GlobalScope.launch(Dispatchers.Default) {

            withContext(Dispatchers.Main) {
                getWishedGame(view)

            }
        }
    }
    private fun getWishedGame(view: View) {
        val wishdGames = mutableListOf<String>()

        val imgL = view.findViewById<ImageView>(R.id.imageStartView)
        val textl = view.findViewById<TextView>(R.id.check_start_text_view)
        val imgL1 = view.findViewById<ImageView>(R.id.imageHeartView)
        val textl1 = view.findViewById<TextView>(R.id.check_heart_text_view)
        val constraint = view.findViewById<ConstraintLayout>(R.id.constraint_id_wish)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar_home)

        // Get reference to the "wishs" child in Firebase
        val wishsRef = database.child("game").child("wishs" )

        // Get a listener for the "wishs" child
        wishsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (wishSnapshot in dataSnapshot.children) {
                    val wish = wishSnapshot.getValue(Wish::class.java)

                    if (wish != null && wish.userId == listGame.userArgs.uid)
                        wishdGames.add(wish.appId)

                }
                progressBar.visibility=View.GONE
                val games: MutableList<Game> = mutableListOf()


                wishdGames.forEach { el->
                    listGame.gameDataArgs.forEach{
                        if( it.appid.toString() == el) {
                            games.add(it!!)
                        }
                    }
                }

                if(games.isNotEmpty()) {
                    if(constraint != null)
                        constraint.visibility=View.GONE
                    if(list_game_wish_recyclerview != null) {
                        list_game_wish_recyclerview.visibility = View.VISIBLE

                        rv = list_game_wish_recyclerview
                        //scroller ver le haut
                        //rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
                        //scroller vers le bas
                        rv.layoutManager = LinearLayoutManager(context)
                        rv.adapter = GamesAdapter(games, listener, getString(R.string.item_price)+ " 10,00€")
                    }
                }else{
                    if(constraint != null)
                        constraint.visibility=View.VISIBLE
                    if(imgL != null)
                        imgL.visibility=View.VISIBLE
                    if(textl != null)
                        textl.visibility=View.VISIBLE
                    if(imgL1 != null)
                        imgL1.visibility=View.GONE
                    if(textl1 != null)
                        textl1.visibility=View.GONE
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
            GameWishFragmentDirections.actionGameWishFragmentToGameDetailFragment(game, listGame.userArgs)
        )

    }
}