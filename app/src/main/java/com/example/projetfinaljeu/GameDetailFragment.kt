package com.example.projetfinaljeu

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Html
import android.view.*
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_game_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameDetailFragment : Fragment() {
    var isItemFavorite:Boolean=true
    var isItemWisk:Boolean=true
    private val game: GameDetailFragmentArgs by navArgs()
   private lateinit var gameInfo: GameInfos
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_game_detail, container, false)

        view.findViewById<TextView>(R.id.textdescri).setOnClickListener {
            it.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.secondary))
            view.findViewById<TextView>(R.id.textnotic)
                .setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.primary))
            description_game.visibility=View.VISIBLE
            list_wish_recyclerview.visibility=View.GONE
        }


        view.findViewById<TextView>(R.id.textnotic).setOnClickListener {
            it.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(), R.color.secondary
                )
            )
            view.findViewById<TextView>(R.id.textdescri).setBackgroundColor(ContextCompat.getColor(
                requireContext(),R.color.primary))
            description_game.visibility=View.GONE
            list_wish_recyclerview.visibility=View.VISIBLE


        }
        return  view;
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar_home)

        val relativeLayout: RelativeLayout = view.findViewById(R.id.text_rel)
        val drawable1 = GradientDrawable()
        drawable1.cornerRadius = 15f // set the corner radius
        val color = ContextCompat.getColor(requireContext(),R.color.secondary)
        drawable1.setColor(color) // set the color using a resource
        relativeLayout.background = drawable1

        progressBar.visibility=View.VISIBLE
        home_detail_frag.visibility=View.GONE



        GlobalScope.launch(Dispatchers.Default) {
            val response = ApiClient.getDetailGames(game.gameDataArgs.appid)
                withContext(Dispatchers.Main) {
                    home_detail_frag.visibility=View.VISIBLE
                    var namegame = response.getAsJsonObject(game.gameDataArgs.appid.toString())
                    var data = namegame.getAsJsonObject("data")
                     gameInfo= GameInfos(game.gameDataArgs.appid,
                        data.get("header_image").asString.trimMargin(),
                        data.get("background").asString.trimMargin(),
                        data.get("background_raw").asString.trimMargin(),
                        data.get("name").asString.trimMargin(),
                        data.getAsJsonArray("publishers").map { it.asString.trimMargin() }.joinToString(),
                        data.get("detailed_description").asString.trimMargin()
                    )

                    val img = view.findViewById<ImageView>(R.id.image_header)
                    val img1 = view.findViewById<ImageView>(R.id.backgr)
                    val img2 = view.findViewById<ImageView>(R.id.id_image_jeu_item)

                    Glide.with(requireContext()).load(gameInfo.header_image).into(img)
                    Glide.with(requireContext()).load(gameInfo.background).into(img1)
                    Glide.with(requireContext()).load(gameInfo.background_raw).into(img2)
                    view.findViewById<TextView>(R.id.title_detail).text = gameInfo.name
                    view.findViewById<TextView>(R.id.descrip).text = gameInfo.publishers
                    view.findViewById<TextView>(R.id.description_game).text = Html.fromHtml(gameInfo.detailed_description)

                progressBar.visibility=View.GONE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.menu_toolbar, menu)
        menu.removeItem(R.id.close)
        menu.removeItem(R.id.logout)

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