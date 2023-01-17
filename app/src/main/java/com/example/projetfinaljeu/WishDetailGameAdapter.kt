package com.example.projetfinaljeu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class WishDetailGameAdapter(
    private val wish: List<WishDetailGame>
) : RecyclerView.Adapter<WishDetailGameViewHolder>() {

    override fun getItemCount(): Int = wish.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishDetailGameViewHolder {
        return WishDetailGameViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_game_detail_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: WishDetailGameViewHolder, position: Int) {
        holder.updateView(wish[position])
    }

}