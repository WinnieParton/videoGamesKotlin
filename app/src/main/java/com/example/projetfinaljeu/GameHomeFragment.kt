package com.example.projetfinaljeu

import android.content.Intent
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.*
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_game_home.*


class GameHomeFragment : Fragment(R.layout.fragment_game_home) {
    private lateinit var games : List<Game>;
    private lateinit var rv:RecyclerView
    private val userArgs: GameHomeFragmentArgs by navArgs()
    private val viewModel by viewModels<SharedViewModel>()
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Firebase.auth.signOut()
                val intent = Intent(requireActivity(), MainActivity::class.java)
                requireActivity().startActivity(intent)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_game_home, container, false)

        sharedViewModel.getData().observe(viewLifecycleOwner, Observer { data ->
            games = data
            println("888888888888888888  "+data.size)
            getGame()
        })

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
        val g = games.get(0)

        val clickinput = view.findViewById<RelativeLayout>(R.id.homeSearchBar)
        clickinput.setOnClickListener {
            // Add action to navigate

            findNavController().navigate(
                GameHomeFragmentDirections.actionGameHomeFragmentToGameDetailFragment(g, userArgs.userArgs)
            )
        }
        btn_read_more.setOnClickListener{

        }
        if(g.header_image?.isNotEmpty() == true || g.header_image != null)
            Glide.with(requireContext())
                .load(g.header_image)
                .into(image)

        if(g.background?.isNotEmpty() == true || g.background != null)
            Glide.with(requireContext())
                .load(g.background)
                .into(id_image_jeu_item_home)

        title_game.text=g.name
        description_game.text=g.detailed_description
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.let {
            it.supportActionBar?.show()
        }
    }

    private fun getGame() {
        rv = list_game_recyclerview
        //scroller ver le haut
        //rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
        //scroller vers le bas
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = GamesAdapter(games, listener, getString(R.string.item_price) + " 10,00€")

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
                    GameHomeFragmentDirections.actionGameHomeFragmentToGameLikeFragment(games.toTypedArray(), userArgs.userArgs))
                return true
            }
            R.id.wish -> {
                findNavController().navigate(
                    GameHomeFragmentDirections.actionGameHomeFragmentToGameWishFragment(games.toTypedArray(), userArgs.userArgs))
                return true
            }
            R.id.logout -> {
                Firebase.auth.signOut()
                val intent = Intent(requireActivity(), MainActivity::class.java)
                requireActivity().startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val listener = GamesAdapter.OnClickListener { game ->
        // Add action to navigate
        findNavController().navigate(
            GameHomeFragmentDirections.actionGameHomeFragmentToGameDetailFragment(game, userArgs.userArgs)
        )

    }
}