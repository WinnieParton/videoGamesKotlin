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
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.fragment_game_research.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


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
                    var dataSearch = mutableListOf<Game>()
                    val newdata = JsonParser().parse(data.toString()).asJsonArray
                    for (jsonElement in newdata) {
                        val it = jsonElement.asJsonObject
                        val response =
                            ApiClient.getDetailGames(it.get("appid").asInt, Locale.getDefault().language)
                        val responseWish = ApiClient.getWishGames(it.get("appid").asInt)

                        val namegame = response.getAsJsonObject(it.get("appid").asInt.toString())
                        val data = namegame.getAsJsonObject("data")
                        var price = data.getAsJsonObject("price_overview")
                            ?.get("final_formatted")?.asString?.trimMargin()
                        if (price != null)
                            price = getString(R.string.item_price) + price;

                        var headerImage = data?.get("header_image")?.asString?.trimMargin()
                        headerImage = getImageUrl(headerImage)

                        var background = data?.get("background")?.asString?.trimMargin()
                        background = getImageUrl(background)


                        var background_raw = data?.get("background_raw")?.asString?.trimMargin()
                        background_raw = getImageUrl(background_raw)

                        println("ffffffffff   $background_raw")
                        dataSearch.add(
                            Game(
                                it.get("appid").asInt,
                                headerImage,
                                background,
                                background_raw,
                                data?.get("name")?.asString?.trimMargin(),
                                data?.getAsJsonArray("publishers")?.joinToString { it.asString.trimMargin() },
                                data?.get("detailed_description")?.asString?.trimMargin(),
                                responseWish.toWishDetailGame()!!,
                                price
                            )
                        )
                    }

                    withContext(Dispatchers.Main) {
                        println("rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr  $data")
                        if(data.isEmpty)
                            list_game_search_recyclerview.visibility=View.GONE
                        else
                            list_game_search_recyclerview.visibility=View.VISIBLE

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

}