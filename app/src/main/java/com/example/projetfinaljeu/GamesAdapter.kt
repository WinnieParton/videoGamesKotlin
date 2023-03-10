package com.example.projetfinaljeu

import android.view.LayoutInflater
import android.view.ViewGroup
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
        holder.updateView(textId, games[position])

        holder.itemView.findViewById<ConstraintLayout>(R.id.item_click_button).setOnClickListener {
            onClickListener.onClick(games[position])
        }
    }

    class OnClickListener(val clickListener: (game: Game) -> Unit) {
        fun onClick(game: Game) = clickListener(game)
    }
}