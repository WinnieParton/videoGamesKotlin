package com.example.projetfinaljeu

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WishDetailGameViewHolder(v: View)  : RecyclerView.ViewHolder(v){
    private val author = v.findViewById<TextView>(R.id.name_user)
    private val descrip_wish = v.findViewById<TextView>(R.id.descrip_wish)
    private val id_start = v.findViewById<RatingBar>(R.id.id_start)
    val colorStateList = ColorStateList.valueOf(Color.YELLOW)

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    fun updateView(wish: WishDetailGame) {
        author.applyUnderlineText(wish.num_games_owned.toString())
        descrip_wish.text=wish.review.trim()
        id_start.rating= wish.votes_up.toFloat()
        id_start.progressTintList = colorStateList
    }
}