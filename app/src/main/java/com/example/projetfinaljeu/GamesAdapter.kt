package com.example.projetfinaljeu

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class GamesAdapter(
    private val games: List<Game>,
    private val onClickListener: OnClickListener,
    private val textId: String?
) : RecyclerView.Adapter<GameViewHolder>() {

    override fun getItemCount(): Int = games.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        return GameViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_game_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val priceText=holder.itemView.findViewById<TextView>(R.id.id_price)
        if (textId != null) {
            priceText.applyUnderlineTextStart(textId)
        }
        holder.itemView.findViewById<ConstraintLayout>(R.id.item_click_button).setOnClickListener {
            onClickListener.onClick(games[position])
        }
    }

    class OnClickListener(val clickListener: (game: Game) -> Unit) {
        fun onClick(game: Game) = clickListener(game)
    }
}