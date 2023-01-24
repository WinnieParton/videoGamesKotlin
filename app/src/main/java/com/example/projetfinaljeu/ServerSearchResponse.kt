package com.example.projetfinaljeu

import com.google.gson.annotations.SerializedName

data class ServerSearchResponse (
    @SerializedName("applist")
    val applist: Applist?
    ){
    data class Applist(
        @SerializedName("apps")
        val apps:  List<ListSearch>?
    ){
        data class ListSearch(
            @SerializedName("name")
            val name: String?,
            @SerializedName("appid")
            val appid: Int?,
        )
    }
    fun toGamesSearch(): List<Game>? =applist?.let{ resp ->
        val prod = mutableListOf<Game>()

        resp.apps?.forEach {
            prod.add(Game(it.appid!!, it.name))
        }

        return prod
    }
}