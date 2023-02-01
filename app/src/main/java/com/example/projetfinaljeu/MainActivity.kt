package com.example.projetfinaljeu

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.projetfinaljeu.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<SharedViewModel>()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = findNavController(R.id.nav_host_fragment_container)

        setupActionBarWithNavController(navController)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragmentContainer: View = findViewById(R.id.nav_host_fragment_container)

        lifecycleScope.launch {
            var listGa = mutableListOf<Game>()
            GlobalScope.launch(Dispatchers.Default) {
                try {
                    binding.prog.progressBarHome.visibility= View.VISIBLE
                    binding.resp.erreurFragment.visibility= View.GONE
                    navHostFragmentContainer.visibility= View.GONE
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
                    binding.resp.erreurFragment.text = "Une erreur est survenue ${e.message}"
                    binding.resp.erreurFragment.visibility= View.VISIBLE
                    binding.prog.progressBarHome.visibility= View.GONE

                    println("Error: ${e.message}")

                }
                withContext(Dispatchers.Main) {
                    viewModel.setGame(listGa)
                    binding.prog.progressBarHome.visibility= View.GONE
                    navHostFragmentContainer.visibility= View.VISIBLE
                    binding.resp.erreurFragment.visibility= View.GONE
                }
            }

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}


fun TextView.applyUnderlineTextStart(text: String){
    if(text!=null) {
        val spannable = SpannableString(text)
        spannable.setSpan(UnderlineSpan(), 0, text.indexOf('€'), 0)
        setText(spannable)
    }
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