package com.example.projetfinaljeu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_game_research.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class GameResearchFragment : Fragment() {
    private val listGame: GameResearchFragmentArgs by navArgs()
    lateinit var rv: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_research, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val actionbar = (activity as AppCompatActivity).supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setHomeAsUpIndicator(R.drawable.close)

        val nbRes=view.findViewById<TextView>(R.id.nb_result)
        nbRes.applyUnderlineTextStart(getString(R.string.nb_result)+listGame.gameDataArgs.toList().size)

        GlobalScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.Main) {
                getGame()
            }
        }
    }

    private fun getGame() {
        val games: List<Game> = listGame.gameDataArgs.toList()
        rv = list_game_search_recyclerview
        //scroller ver le haut
        //rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
        //scroller vers le bas
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = GamesAdapter(games, listener, getString(R.string.item_price))

    }

    private val listener = GamesAdapter.OnClickListener { game ->
        // Add action to navigate
        findNavController().navigate(
            GameResearchFragmentDirections.actionGameResearchFragmentToGameDetailFragment(game)
        )

    }
}