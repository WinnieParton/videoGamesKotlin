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
    private val descripwish = v.findViewById<TextView>(R.id.descrip_wish)
    private val idstart = v.findViewById<RatingBar>(R.id.id_start)
    private val colorStateList = ColorStateList.valueOf(Color.YELLOW)

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    fun updateView(wish: WishDetailGame) {
        author.applyUnderlineText(wish.num_games_owned.toString())
        descripwish.text=wish.review.trim()
        idstart.rating= wish.votes_up.toFloat()
        idstart.progressTintList = colorStateList
    }
}