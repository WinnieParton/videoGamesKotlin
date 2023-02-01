package com.example.projetfinaljeu

import android.content.Intent
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_game_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


private fun Any.observe(viewLifecycleOwner: LifecycleOwner, observer: Observer) {
    TODO("Not yet implemented")
}

class GameHomeFragment : Fragment(R.layout.fragment_game_home) {
    private lateinit var games : List<Game>;
    private var listGa = mutableListOf<Game>()
    private lateinit var rv:RecyclerView
    private val userArgs: GameHomeFragmentArgs by navArgs()
    private val viewModel by activityViewModels<SharedViewModel>()

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
        val clickinput = view.findViewById<RelativeLayout>(R.id.homeSearchBar)
        clickinput.setOnClickListener {
            // Add action to navigate
            findNavController().navigate(
                GameHomeFragmentDirections.actionGameHomeFragmentToGameResearchFragment(games.toTypedArray(), userArgs.userArgs)
            )
        }
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar_home)
        //val errorr = view.findViewById<TextView>(R.id.erreur_fragment)
        val color = ContextCompat.getColor(requireContext(), R.color.white)
        val drawable = progressBar.indeterminateDrawable.mutate()
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        progressBar.indeterminateDrawable = drawable

        progressBar.visibility=View.VISIBLE

        home_frag.visibility=View.GONE
        //errorr.visibility=View.GONE

         GlobalScope.launch(Dispatchers.Default) {
             try {

             val response = ApiClient.getGames()
             response.toRanks()?.let { println(it.size) }
             response.toRanks()?.forEach {
                 val response = ApiClient.getDetailGames(it.appid!!, Locale.getDefault().language)
                 val responseWish = ApiClient.getWishGames(it.appid)


                 val namegame = response.getAsJsonObject(it.appid.toString())
                 val data = namegame.getAsJsonObject("data")
                 var price = data.getAsJsonObject("price_overview")?.get("final_formatted")?.asString?.trimMargin()
                if(price != null)
                    price = getString(R.string.item_price)+" "+price

                 var headerImage = data?.get("header_image")?.asString?.trimMargin()
                 headerImage = getImageUrl(headerImage)

                 var background=data?.get("background")?.asString?.trimMargin()
                 background = getImageUrl(background)


                 var background_raw = data?.get("background_raw")?.asString?.trimMargin()
                 background_raw = getImageUrl(background_raw)

                println("ffffffffff   $background_raw")
                 listGa.add(Game(
                     it.appid,
                     headerImage,
                     background,
                     background_raw ,
                     data?.get("name")?.asString?.trimMargin(),
                     data?.getAsJsonArray("publishers")?.joinToString { it.asString.trimMargin() },
                     data?.get("detailed_description")?.asString?.trimMargin(),
                     responseWish.toWishDetailGame()!!,
                     price)
                 )
                 }

             } catch (e: Exception) {
                 println("Error: ${e.message}")
                 //errorr.visibility=View.VISIBLE
                 //errorr.text = getString(R.string.error) + e.message

             }
             withContext(Dispatchers.Main) {
                 getGame(listGa)
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

    private fun getGame(listGame: List<Game>) {
        games = listGame
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