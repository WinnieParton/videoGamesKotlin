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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_game_detail.*
import kotlinx.android.synthetic.main.fragment_game_detail.description_game
import kotlinx.android.synthetic.main.fragment_game_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class GameDetailFragment : Fragment() {
    var isItemFavorite:Boolean=true
    var isItemWisk:Boolean=true
    private val game: GameDetailFragmentArgs by navArgs()
    private lateinit var rv: RecyclerView

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
            val responseWish = ApiClient.getWishGames(game.gameDataArgs.appid)

                withContext(Dispatchers.Main) {
                    getGameWishDetail(responseWish)
                    home_detail_frag.visibility=View.VISIBLE
                    val namegame = response.getAsJsonObject(game.gameDataArgs.appid.toString())
                    val data = namegame.getAsJsonObject("data")

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

                    val url = URL(gameInfo.header_image)
                    val url1 = URL(gameInfo.background_raw)
                    val url2 = URL(gameInfo.background)

                    try {
                        val connection = url.openConnection() as HttpURLConnection
                        connection.connect()
                        if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                            // URL is valid, load image with Glide
                            Glide.with(requireContext()).load(gameInfo.header_image).into(img)
                        }

                        val connection1 = url1.openConnection() as HttpURLConnection
                        connection1.connect()
                        if (connection1.responseCode == HttpURLConnection.HTTP_OK) {
                            // URL is valid, load image with Glide
                            Glide.with(requireContext()).load(gameInfo.background_raw).into(img2)
                        }

                        val connection2 = url2.openConnection() as HttpURLConnection
                        connection2.connect()
                        if (connection2.responseCode == HttpURLConnection.HTTP_OK) {
                            // URL is valid, load image with Glide
                            Glide.with(requireContext()).load(gameInfo.background).into(img1)
                        }
                    } catch (e: Exception) {
                        // URL is invalid, show default image or handle the error
                    }
                    view.findViewById<TextView>(R.id.title_detail).text = gameInfo.name
                    view.findViewById<TextView>(R.id.descrip).text = gameInfo.publishers
                    view.findViewById<TextView>(R.id.description_game).text = Html.fromHtml(gameInfo.detailed_description)

                progressBar.visibility=View.GONE
            }
        }
    }

    private fun getGameWishDetail(response: ServerDetailWishGameResponse) {
        val wish: List<WishDetailGame> = response.toWishDetailGame()!!
        rv = list_wish_recyclerview
        //scroller ver le haut
        rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
        //scroller vers le bas
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = WishDetailGameAdapter(wish)

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