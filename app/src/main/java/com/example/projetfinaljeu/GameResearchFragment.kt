package com.example.projetfinaljeu

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
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
    private lateinit var rv: RecyclerView
    private lateinit var games: List<Game>
    private lateinit var gamesSearch: List<Game>
    private val userArgs: GameResearchFragmentArgs by navArgs()

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

        val constraint = view.findViewById<ConstraintLayout>(R.id.constraint_id)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar_home)

        progressBar.visibility=View.VISIBLE

        constraint.visibility=View.GONE

        val searchEditText = view.findViewById<EditText>(R.id.text_research_input)

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

                GlobalScope.launch(Dispatchers.Default) {
                    val data = ApiClient.getGamesResearch(s.toString())

                    withContext(Dispatchers.Main) {
                        println("rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr  $data")
                        if(data.isEmpty)
                            list_game_search_recyclerview.visibility=View.GONE
                        else
                            list_game_search_recyclerview.visibility=View.VISIBLE
                        //val dataSearch = searchGames(data)
                        //getGame(dataSearch, view)
                        if(nb_result !=null)
                            nb_result.applyUnderlineTextPart(getString(R.string.nb_result)+data.size())
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        GlobalScope.launch(Dispatchers.Default) {
            gamesSearch = userArgs.gameDataArgs.toList()

            withContext(Dispatchers.Main) {
                if(nb_result !=null)
                    nb_result.applyUnderlineTextPart(getString(R.string.nb_result)+gamesSearch.size)
                getGame(gamesSearch, view)
            }
        }
    }

    private fun getGame(response:  List<Game>, view: View) {
        games = response
        val constraint = view.findViewById<ConstraintLayout>(R.id.constraint_id)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar_home)
        if(games.isNotEmpty()){
            if(no_game != null)
                no_game.visibility=View.GONE
            if(list_game_search_recyclerview != null) {
                rv = list_game_search_recyclerview
                //scroller vers le bas
                rv.layoutManager = LinearLayoutManager(context)
                rv.adapter = GamesAdapter(games, listener, getString(R.string.item_price))
            }

        }else{
            if(no_game != null)
                no_game.visibility=View.VISIBLE
        }
        if(progressBar != null)
            progressBar.visibility=View.GONE

        if(constraint != null)
            constraint.visibility=View.VISIBLE


    }

    private val listener = GamesAdapter.OnClickListener { game ->
        // Add action to navigate
        findNavController().navigate(
            GameResearchFragmentDirections.actionGameResearchFragmentToGameDetailFragment(game, userArgs.userArgs)
        )

    }
    /*private fun searchGames(data: JsonArray): List<Game> {

        return userArgs.gameDataArgs.filter { obj ->
            data.toList().any { obj2 ->
                obj.appid == obj2["dd"]
            }
        }
    }*/
}