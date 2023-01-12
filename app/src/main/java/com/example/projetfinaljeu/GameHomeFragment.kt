package com.example.projetfinaljeu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projetfinaljeu.databinding.FragmentGameHomeBinding
import kotlinx.android.synthetic.main.fragment_game_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class GameHomeFragment : Fragment(R.layout.fragment_game_home) {
   // private val login: GameHomeFragmentArgs by navArgs()
   lateinit var rv:RecyclerView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // System.out.println("infos de connexion "+login)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_home, container, false)
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
        rv.adapter = GamesAdapter(games, listener)

    }
    private val listener = GamesAdapter.OnClickListener { game ->
        // Add action to navigate
        findNavController().navigate(
            GameHomeFragmentDirections.actionGameHomeFragmentToGameDetailFragment(game)
        )

    }
}