package com.example.projetfinaljeu

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class GameViewHolder(v: View)  : RecyclerView.ViewHolder(v){
    private val priceText = v.findViewById<TextView>(R.id.id_price)
    private val title_gameText = v.findViewById<TextView>(R.id.title_game)
    private val name_editeurText = v.findViewById<TextView>(R.id.name_editeur)
    private val id_image_jeu_itemImg = v.findViewById<ImageView>(R.id.id_image_jeu_item)
    private val imgheaderbackImg = v.findViewById<ImageView>(R.id.imgheaderback)

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    fun updateView(textId:String?, game: Game) {
        if (game.price_overview != null) {
            priceText.applyUnderlineTextStart(game.price_overview)
        }else
            priceText.applyUnderlineTextStart(textId!!)

        title_gameText.text=game.name
        name_editeurText.text=game.publishers

        if(game.header_image != null || game.header_image != "" )
            Glide.with(itemView)
                .load(game.header_image)
                .into(id_image_jeu_itemImg)

        if(game.background != null)
            Glide.with(itemView)
                .load(game.background)
                .into(imgheaderbackImg)
    }
}