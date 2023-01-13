package com.example.projetfinaljeu

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_game_home.*


class GameHomeFragment : Fragment(R.layout.fragment_game_home) {
   // private val login: GameHomeFragmentArgs by navArgs()

   lateinit var rv:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game_home, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getGame()
        /* GlobalScope.launch(Dispatchers.Default) {

             val response = ApiClient.getGames()

             withContext(Dispatchers.Main) {
                 getGame(response);
             }
         }*/
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.let {
            it.supportActionBar?.show()

        }
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
        rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
        //scroller vers le bas
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = GamesAdapter(games, listener, getString(R.string.item_price))

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.menu_toolbar, menu)
        menu.removeItem(R.id.close);

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.like -> {
                findNavController().navigate(
                    GameHomeFragmentDirections.actionGameHomeFragmentToGameLikeFragment(arrayOf(
                        Game(123),
                        Game(855), Game(955), Game(855),
                        Game(955),Game(855), Game(955)))
                )
                return true
            }
            R.id.wish -> {
                findNavController().navigate(
                    GameHomeFragmentDirections.actionGameHomeFragmentToGameWishFragment(arrayOf(Game(123), Game(855), Game(955)))
                )
                return true
            }
            R.id.logout -> {
                findNavController().navigate(
                    GameHomeFragmentDirections.actionGameHomeFragmentToGameLoginFragment()
                )
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val listener = GamesAdapter.OnClickListener { game ->
        // Add action to navigate
        findNavController().navigate(
            GameHomeFragmentDirections.actionGameHomeFragmentToGameDetailFragment(game)
        )

    }
}