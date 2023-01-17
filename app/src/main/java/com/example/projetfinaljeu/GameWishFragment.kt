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
import kotlinx.android.synthetic.main.fragment_game_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameWishFragment : Fragment() {
    private val listGame: GameWishFragmentArgs by navArgs()
    lateinit var rv: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_wish, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actionbar = (activity as AppCompatActivity).supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setHomeAsUpIndicator(R.drawable.close)


        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar_home)
        val imgL = view.findViewById<ImageView>(R.id.imageStartView)
        val textl = view.findViewById<TextView>(R.id.check_start_text_view)
        val imgL1 = view.findViewById<ImageView>(R.id.imageHeartView)
        val textl1 = view.findViewById<TextView>(R.id.check_heart_text_view)
        val constraint = view.findViewById<ConstraintLayout>(R.id.constraint_id_wish)
        progressBar.visibility=View.VISIBLE

        list_game_recyclerview.visibility=View.GONE
        GlobalScope.launch(Dispatchers.Default) {

            val games: List<Game> = listOf() // listGame.gameDataArgs.toList()

            withContext(Dispatchers.Main) {
                progressBar.visibility=View.GONE
                if(games.isNotEmpty()) {
                    constraint.visibility=View.GONE
                    list_game_recyclerview.visibility=View.VISIBLE

                    rv = list_game_recyclerview
                    //scroller ver le haut
                    //rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
                    //scroller vers le bas
                    rv.layoutManager = LinearLayoutManager(context)
                    rv.adapter = GamesAdapter(games, listener, getString(R.string.item_price))
                }else{
                    constraint.visibility=View.VISIBLE
                    imgL.visibility=View.VISIBLE
                    textl.visibility=View.VISIBLE
                    imgL1.visibility=View.GONE
                    textl1.visibility=View.GONE

                }
            }


        }



    }

    private val listener = GamesAdapter.OnClickListener { game ->
        // Add action to navigate
        findNavController().navigate(
            GameWishFragmentDirections.actionGameWishFragmentToGameDetailFragment(game)
        )

    }
}