package com.example.projetfinaljeu

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Html
import android.util.Log
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_game_detail.*
import kotlinx.android.synthetic.main.fragment_game_detail.description_game
import kotlinx.android.synthetic.main.fragment_game_home.*
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL

class GameDetailFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance().reference
    private var isItemFavorite:Boolean=false
    private var isItemWisk:Boolean=false
    private val game: GameDetailFragmentArgs by navArgs()
    private lateinit var rv: RecyclerView
    private lateinit var gameInfo: GameInfos
    private var isEmptyWish:Boolean=false
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
            no_wish.visibility=View.GONE

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
            if(isEmptyWish)
                no_wish.visibility=View.VISIBLE
        }
        return  view
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
                val namegame = response.getAsJsonObject(game.gameDataArgs.appid.toString())
                val data = namegame.getAsJsonObject("data")
                 gameInfo= GameInfos(game.gameDataArgs.appid,
                     data?.get("header_image")?.asString?.trimMargin(),
                    data?.get("background")?.asString?.trimMargin(),
                    data?.get("background_raw")?.asString?.trimMargin(),
                    data?.get("name")?.asString?.trimMargin(),
                    data?.getAsJsonArray("publishers")?.joinToString { it.asString.trimMargin() },
                    data?.get("detailed_description")?.asString?.trimMargin()
                )
                updateImg(gameInfo, view)

            }
        }
    }

    private fun updateImg(gameIfo: GameInfos, view: View) {
        val img = view.findViewById<ImageView>(R.id.image_header)
        val img1 = view.findViewById<ImageView>(R.id.backgr)
        val img2 = view.findViewById<ImageView>(R.id.id_image_jeu_item)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar_home)
        var data = 404
        var data1 = 404
        var data2 = 404
        GlobalScope.launch {

             if(gameIfo.header_image != null){
                 val url =   URL(gameIfo.header_image.split("?")[0])
                 val connection = withContext(Dispatchers.IO) {
                     url.openConnection()
                 } as HttpURLConnection

                 withContext(Dispatchers.IO) {
                     connection.connect()
                 }

                 data = connection.responseCode
             }


            if(gameIfo.background != null) {
                val url1 = URL(gameIfo.background.split("?")[0])

                val connection1 = withContext(Dispatchers.IO) {
                    url1.openConnection()
                } as HttpURLConnection
                withContext(Dispatchers.IO) {
                    connection1.connect()
                }

                data1 = connection1.responseCode
            }

            if(gameIfo.header_image != null) {
                val url2 = URL(gameIfo.header_image.split("?")[0])


                val connection2 = withContext(Dispatchers.IO) {
                    url2.openConnection()
                } as HttpURLConnection
                withContext(Dispatchers.IO) {
                    connection2.connect()
                }

                data2 = connection2.responseCode
            }
            withContext(Dispatchers.Main) {
                if (data == HttpURLConnection.HTTP_OK) {
                    Glide.with(requireContext())
                        .load(gameIfo.header_image?.split("?")!![0])
                        .into(img)
                }

                if (data1 == HttpURLConnection.HTTP_OK) {
                    Glide.with(requireContext())
                        .load(gameIfo.background?.split("?")!![0])
                        .into(img1)
                }

                if (data2 == HttpURLConnection.HTTP_OK) {
                    Glide.with(requireContext())
                        .load(gameIfo.background_raw?.split("?")?.get(0))
                        .into(img2)
                }

                if(gameIfo.name !=null)
                    view.findViewById<TextView>(R.id.title_detail).text = gameIfo.name
                if(gameIfo.publishers !=null)
                    view.findViewById<TextView>(R.id.descrip).text = gameIfo.publishers
                if(gameIfo.detailed_description !=null)
                    view.findViewById<TextView>(R.id.description_game).text = Html.fromHtml(gameIfo.detailed_description)
                else
                    view.findViewById<TextView>(R.id.description_game).text = getString(R.string.no_description)
                progressBar.visibility = View.GONE
                home_detail_frag.visibility = View.VISIBLE
            }
        }
    }

    private fun getGameWishDetail(response: ServerDetailWishGameResponse) {
        val wish: List<WishDetailGame> = response.toWishDetailGame()!!
        if(wish.isNotEmpty()) {
            rv = list_wish_recyclerview
            rv.layoutManager = LinearLayoutManager(context)
            rv.adapter = WishDetailGameAdapter(wish)
        }else
            isEmptyWish=true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.menu_toolbar, menu)
        menu.removeItem(R.id.close)
        menu.removeItem(R.id.logout)
        getExistsWish(menu.findItem(R.id.wish))
        getExistsLike(menu.findItem(R.id.like))
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        auth = Firebase.auth
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.like -> {
                if (isItemFavorite) {
                    item.setIcon(R.drawable.like)
                    isItemFavorite = true
                    unlikeGame()
                } else {
                    item.setIcon(R.drawable.like_full)
                    isItemFavorite = false
                    likeGame()
                }
                return true
            }
            R.id.wish -> {
                if (isItemWisk) {
                    item.setIcon(R.drawable.whishlist)
                    isItemWisk = true
                    unwishGame()
                } else {
                    item.setIcon(R.drawable.whishlist_full)
                    isItemWisk = false
                    wishGame()
                }

                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun likeGame(){
        // Create a "like" object to save in Firebase
        val like = mapOf("userId" to game.userArgs.uid, "appId" to game.gameDataArgs.appid.toString())

        // Save the "like" object to Firebase under the video's ID
        database.child("game").child("likes").push().setValue(like)
    }

    private fun unlikeGame(){
        // Get reference to the video's "likes" child in Firebase
        val likesRef = database.child("game").child("likes" )

        // Get a listener for the "likes" child
        likesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (likeSnapshot in dataSnapshot.children) {
                    val like = likeSnapshot.getValue(Like::class.java)
                    if (like != null && like.appId == game.gameDataArgs.appid.toString() && like.userId == game.userArgs.uid) {
                        // Remove the like from Firebase
                        likeSnapshot.ref.removeValue()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun wishGame(){
        // Create a "like" object to save in Firebase
        val like = mapOf("userId" to game.userArgs.uid, "appId" to game.gameDataArgs.appid.toString())

        // Save the "like" object to Firebase under the video's ID
        database.child("game").child("wishs").push().setValue(like)
    }

    private fun unwishGame(){

        // Get reference to the video's "likes" child in Firebase
        val wishsRef = database.child("game").child("wishs" )

        // Get a listener for the "likes" child
        wishsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (wishSnapshot in dataSnapshot.children) {
                    val wish = wishSnapshot.getValue(Wish::class.java)
                    if (wish != null && wish.appId == game.gameDataArgs.appid.toString() && wish.userId == game.userArgs.uid) {
                        // Remove the like from Firebase
                        wishSnapshot.ref.removeValue()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun getExistsWish(menItem: MenuItem){

        val wishsRef = database.child("game").child("wishs" )
        wishsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (wishSnapshot in dataSnapshot.children) {
                    val wish = wishSnapshot.getValue(Wish::class.java)

                    if (wish != null && wish.appId == game.gameDataArgs.appid.toString() && wish.userId == game.userArgs.uid) {
                        menItem.setIcon(R.drawable.whishlist_full)
                        isItemWisk = true

                    }
                }

            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("Error", "Error getting child", error.toException())
            }
        })
    }

    private fun getExistsLike(menItem: MenuItem){

        val likesRef = database.child("game").child("likes" )
        likesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (likeSnapshot in dataSnapshot.children) {
                    val like = likeSnapshot.getValue(Like::class.java)

                    if (like != null && like.appId == game.gameDataArgs.appid.toString() && like.userId == game.userArgs.uid) {
                        menItem.setIcon(R.drawable.like_full)
                        isItemFavorite = true
                    }
                }

            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("Error", "Error getting child", error.toException())
            }
        })
    }
}