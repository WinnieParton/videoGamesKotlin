package com.example.projetfinaljeu

import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.android.synthetic.main.fragment_game_home.*
import kotlinx.android.synthetic.main.item_game_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import com.example.projetfinaljeu.SharedViewModel as SharedViewModel1


class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    @RequiresApi(Build.VERSION_CODES.S)


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = findNavController(R.id.nav_host_fragment_container)

        setupActionBarWithNavController(navController)


        lifecycleScope.launch {

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
                            price = getString(R.string.item_price)+price;

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
                    progressBar.visibility= View.GONE
                    home_frag.visibility= View.VISIBLE
                }
            }
            viewModel.game.value = game
        }

            //pour rendre le loader visible
        //progressBar.visibility=View.VISIBLE
        //pour rendre invisible
       // progressBar.visibility=View.GONE

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}


fun TextView.applyUnderlineTextStart(text: String){
    val spannable = SpannableString(text)
    spannable.setSpan(UnderlineSpan(),  0,text.indexOf('€')-1 , 0)
    setText(spannable)
}
fun TextView.applyUnderlineText(text: String){
    if(text!=null) {
        val spannable = SpannableString(text)
        spannable.setSpan(UnderlineSpan(), 0, text.length, 0)
        setText(spannable)
    }
}

fun TextView.applyUnderlineTextPart(text: String){
    val spannable = SpannableString(text)
    spannable.setSpan(UnderlineSpan(),  0,text.indexOf(':') , 0)
    setText(spannable)
}