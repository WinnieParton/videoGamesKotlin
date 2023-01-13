package com.example.projetfinaljeu

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.renderscript.Allocation
import android.renderscript.ScriptIntrinsicBlur
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_game_home.*


class GameHomeFragment : Fragment(R.layout.fragment_game_home) {
   // private val login: GameHomeFragmentArgs by navArgs()

   lateinit var rv:RecyclerView;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game_home, container, false)
       /* val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.close)*/
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getGame();
       /* GlobalScope.launch(Dispatchers.Default) {

            val response = ApiClient.getGames()

            withContext(Dispatchers.Main) {
                getGame(response);
            }
        }*/
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }
    private fun getGame() {
//response: ServerResponse
        val games: List<Game> = listOf(Game(1),
            Game(6),
            Game(8),Game(6),
            Game(8),Game(6),
            Game(8))
  rv = list_game_recyclerview
        //scroller ver le haut
        rv.layoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true)
        //scroller vers le bas
        rv.layoutManager = LinearLayoutManager(getContext())
        rv.adapter = GamesAdapter(games, listener, getString(R.string.item_price))

    }
    private val listener = GamesAdapter.OnClickListener { game ->
        // Add action to navigate
        findNavController().navigate(
            GameHomeFragmentDirections.actionGameHomeFragmentToGameDetailFragment(game)
        )

    }
}