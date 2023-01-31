package com.example.projetfinaljeu

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    //liste  des jeux les plus joués
    @GET("ISteamChartsService/GetMostPlayedGames/v1/?")
    fun getGamesAsync(): Deferred<ServerResponse>

    //recuperer les details d'un jeu
    @GET("api/appdetails")
    fun getDetailGamesAsync(@Query("appids") appids: Int?, @Query("l") lang: String?): Deferred<JsonObject>

    //recuperer les avis d'un jeu
    @GET("appreviews/{apiId}?json=1")
    fun getWishGamesAsync(@Path("apiId") apiId: Int?): Deferred<ServerDetailWishGameResponse>

    //liste  des jeux sur lesquels la recherche s'applique
    @GET("SearchApps/{textsearch}")
    fun getGamesResearchAsync(@Path("textsearch") textsearch: String?): Deferred<JsonArray>
}