package com.example.projetfinaljeu

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    //liste  des jeux les plus joués
    @GET("GetMostPlayedGames/v1/?")
    fun getGames(): Deferred<ServerResponse>

    //recuperer les details d'un jeu
    @GET("api/appdetails")
    fun getDetailGames(@Query("appids") appids: Int?): Deferred<ServerResponse>

    //recuperer les avis d'un jeu
    @GET("appreviews/{apiId}?json=1")
    fun getWishGames(@Path("apiId") apiId: Int?): Deferred<ServerResponse>
}