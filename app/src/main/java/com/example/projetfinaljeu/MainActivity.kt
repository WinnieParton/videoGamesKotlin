package com.example.projetfinaljeu

import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private val viewModel by viewModels<SharedViewModel>()
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = findNavController(R.id.nav_host_fragment_container)

        /*lifecycleScope.launch {
            var listGa = mutableListOf<Game>()
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
                    viewModel.setData(listGa)

                }
            }

        }*/


        setupActionBarWithNavController(navController)

     }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}


fun TextView.applyUnderlineTextStart(text: String){
    val spannable = SpannableString(text)
    spannable.setSpan(UnderlineSpan(), 0, text.indexOf('€'), 0)
    setText(spannable)
}
fun TextView.applyUnderlineText(text: String){
    val spannable = SpannableString(text)
    spannable.setSpan(UnderlineSpan(), 0, text.length, 0)
    setText(spannable)
}

fun TextView.applyUnderlineTextPart(text: String){
    val spannable = SpannableString(text)
    spannable.setSpan(UnderlineSpan(),  0,text.indexOf(':') , 0)
    setText(spannable)
}

fun getImageUrl(imageUrl: String?): String {
    var processedUrl = ""
    if (imageUrl != null) {
        val url = imageUrl.split("?").firstOrNull() ?: imageUrl
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.connect()
        if (connection.responseCode == HttpURLConnection.HTTP_OK) {
            processedUrl = imageUrl.split("?").first()
        }
    }
    return processedUrl
}