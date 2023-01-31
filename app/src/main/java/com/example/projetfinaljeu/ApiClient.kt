package com.example.projetfinaljeu

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private val apisearch = Retrofit.Builder()
        .baseUrl("https://steamcommunity.com/actions/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(ApiInterface::class.java)

    private val api = Retrofit.Builder()
        .baseUrl("https://api.steampowered.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(ApiInterface::class.java)

    private val apiwish = Retrofit.Builder()
        .baseUrl("https://store.steampowered.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(ApiInterface::class.java)



   suspend fun getGames(): ServerResponse {
        return api.getGamesAsync().await()
   }

    suspend fun getDetailGames(apiId: Int, lang: String): JsonObject {
        return apiwish.getDetailGamesAsync(apiId, lang).await()
    }

    suspend fun getWishGames(apiId: Int): ServerDetailWishGameResponse {
        return apiwish.getWishGamesAsync(apiId).await()
    }

    suspend fun getGamesResearch(textsearch: String): JsonArray {
        return apisearch.getGamesResearchAsync(textsearch).await()
    }
}