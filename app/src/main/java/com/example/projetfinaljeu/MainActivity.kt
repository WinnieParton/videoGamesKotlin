package com.example.projetfinaljeu

import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.android.synthetic.main.item_game_list.*


class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = findNavController(R.id.nav_host_fragment_container)

        setupActionBarWithNavController(navController)

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
    spannable.setSpan(UnderlineSpan(),  0,text.indexOf(' ') , 0)
    setText(spannable)
}
fun TextView.applyUnderlineText(text: String){
    if(text!=null) {
        val spannable = SpannableString(text)
        spannable.setSpan(UnderlineSpan(), 0, text.length, 0)
        setText(spannable)
    }
}