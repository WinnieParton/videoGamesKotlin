package com.example.projetfinaljeu

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val prog = findViewById<ProgressBar>(R.id.progress_bar_home)
        val resp  = findViewById<TextView>(R.id.erreur_fragment)
        val sharedViewModel: SharedViewModel by lazy {
            ViewModelProvider(this).get(SharedViewModel::class.java)
        }
        lifecycleScope.launch {
            var listGa = mutableListOf<Game>()
            GlobalScope.launch(Dispatchers.Default) {
                try {
                    prog.visibility= View.VISIBLE
                    resp.visibility= View.GONE
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
                            price))
                    }

                } catch (e: Exception) {
                    //resp.text = "Une erreur est survenue ${e.message}"
                    //resp.visibility= View.VISIBLE
                    //prog.visibility= View.GONE

                    println("Error: ${e.message}")

                }
                withContext(Dispatchers.Main) {
                    println("88888888888888888  "+listGa.size)
                    sharedViewModel.games = listGa
                    val intent = Intent(this@SplashActivity,MainActivity::class.java)
                    startActivity(intent)
                    finish()

                }
            }

        }
    }

    private fun splitArrayList(games: List<Game>): List<List<Game>> {
        val result = mutableListOf<List<Game>>()
        val chunkSize = (1107828 / 64 ) - 1
        for (i in 0 until games.size step chunkSize) {
            val chunk = games.subList(i, Math.min(i + chunkSize, games.size))
            result.add(chunk)
        }
        return result
    }


}
