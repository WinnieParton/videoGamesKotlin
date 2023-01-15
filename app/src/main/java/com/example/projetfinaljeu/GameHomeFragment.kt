package com.example.projetfinaljeu

import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_game_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class GameHomeFragment : Fragment(R.layout.fragment_game_home) {
   // private val login: GameHomeFragmentArgs by navArgs()

    private lateinit var rv:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game_home, container, false)

        val actionbar = (activity as AppCompatActivity).supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(false)

        val textView: TextView = view.findViewById(R.id.title_underline)
        textView.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        val constraintLayout: ConstraintLayout = view.findViewById(R.id.btn_read_more)
        val drawable = GradientDrawable()
        drawable.cornerRadius = 15f // set the corner radius
        drawable.setColor(ContextCompat.getColor(requireContext(),R.color.secondary))
        constraintLayout.background = drawable

        val relativeLayout: RelativeLayout = view.findViewById(R.id.homeSearchBar)
        val drawable1 = GradientDrawable()
        drawable1.cornerRadius = 15f // set the corner radius
        val color = ContextCompat.getColor(requireContext(),R.color.third)
        drawable1.setColor(color) // set the color using a resource
        relativeLayout.background = drawable1


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar_home)
        val color = ContextCompat.getColor(requireContext(), R.color.white)
        val drawable = progressBar.indeterminateDrawable.mutate()
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        progressBar.indeterminateDrawable = drawable

        progressBar.visibility=View.VISIBLE

        home_frag.visibility=View.GONE

         GlobalScope.launch(Dispatchers.Default) {

             val response = ApiClient.getGames()

             withContext(Dispatchers.Main) {
                 getGame(response);
                 progressBar.visibility=View.GONE
                 home_frag.visibility=View.VISIBLE
             }
         }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.let {
            it.supportActionBar?.show()

        }
    }

    private fun getGame(response: ServerResponse) {
        val games: List<Game> = response.toGames()!!
        rv = list_game_recyclerview
        //scroller ver le haut
        rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
        //scroller vers le bas
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = GamesAdapter(games, listener, getString(R.string.item_price))

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.menu_toolbar, menu)
        menu.removeItem(R.id.close)

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