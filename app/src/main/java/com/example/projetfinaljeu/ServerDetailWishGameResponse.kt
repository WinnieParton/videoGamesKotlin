package com.example.projetfinaljeu

import com.google.gson.annotations.SerializedName

data class ServerDetailWishGameResponse (
    @SerializedName("reviews")
    val reviews: List<Reviews>?
    ){
    data class Reviews(
        @SerializedName("author")
        val author: Author?,
        @SerializedName("votes_up")
        val votes_up: Int?,
        @SerializedName("review")
        val review:String?,
    ){
        data class Author(
            @SerializedName("steamid")
            val steamid: String?,
            @SerializedName("num_games_owned")
            val num_games_owned: Int?
        )
    }

    fun toWishDetailGame(): List<WishDetailGame>? =reviews?.let{ resp ->
        var prod = mutableListOf<WishDetailGame>()

        resp.forEach {
            prod.add(WishDetailGame(it.author!!.steamid!!, it.author.num_games_owned!!, it.votes_up!!, it.review!!))
        }

        return prod
    }
}