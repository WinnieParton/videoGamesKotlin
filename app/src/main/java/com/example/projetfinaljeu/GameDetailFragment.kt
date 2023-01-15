package com.example.projetfinaljeu

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameDetailFragment : Fragment() {
    var isItemFavorite:Boolean=true;
    var isItemWisk:Boolean=true;
    private val game: GameDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_detail, container, false)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar_home)

        progressBar.visibility=View.VISIBLE

        GlobalScope.launch(Dispatchers.Default) {
            val response = ApiClient.getDetailGames(game.gameDataArgs.appid)
                withContext(Dispatchers.Main) {
                    var namegame = response.getAsJsonObject(game.gameDataArgs.appid.toString())
                    var data = namegame.getAsJsonObject("data")
                    var gameInfo= GameInfos(game.gameDataArgs.appid,
                        data.get("header_image").toString(),
                        data.get("background").toString(),
                        data.get("background_raw").toString(),
                        data.get("name").toString(),
                        data.getAsJsonArray("publishers").joinToString (", "),
                        data.get("detailed_description").toString()
                    )

                progressBar.visibility=View.GONE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.menu_toolbar, menu)
        menu.removeItem(R.id.close);
        menu.removeItem(R.id.logout);

        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.like -> {
                if (isItemFavorite) {
                    item.setIcon(R.drawable.like_full)
                    isItemFavorite = false
                } else {
                    item.setIcon(R.drawable.like)
                    isItemFavorite = true
                }
                return true
            }
            R.id.wish -> {
                if (isItemWisk) {
                    item.setIcon(R.drawable.whishlist_full)
                    isItemWisk = false
                } else {
                    item.setIcon(R.drawable.whishlist)
                    isItemWisk = true
                }

                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}