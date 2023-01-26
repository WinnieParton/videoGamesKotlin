package com.example.projetfinaljeu

import com.google.gson.annotations.SerializedName

data class ServerResponse (
    @SerializedName("response")
    val response: Response?
    ){
    data class Response(
        @SerializedName("ranks")
        val ranks: List<Rank>?
    ){
        data class Rank(
            @SerializedName("appid")
            val appid: Int?,
        )
    }
    fun toGames(): List<Game>? =response?.let{ resp ->
        val prod = mutableListOf<Game>()

        resp.ranks?.forEach {
            prod.add(Game(it.appid!!, ""))
        }

        return prod
    }
}