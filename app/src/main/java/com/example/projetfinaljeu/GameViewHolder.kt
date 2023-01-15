package com.example.projetfinaljeu

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GameViewHolder(v: View)  : RecyclerView.ViewHolder(v){
    private val priceText = v.findViewById<TextView>(R.id.id_price)
    private val imageView: ImageView = v.findViewById(R.id.id_image_jeu_item)

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    fun updateView(game: Game, textId:String) {
        priceText.applyUnderlineTextStart(textId)

    }
}