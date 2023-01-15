package com.example.projetfinaljeu

import com.google.gson.JsonObject
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private val api = Retrofit.Builder()
        .baseUrl("https://api.steampowered.com/ISteamChartsService/")
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
        return api.getGames().await()
   }

    suspend fun getDetailGames(apiId: Int): JsonObject {
        return apiwish.getDetailGames(apiId).await()
    }

    suspend fun getWishGames(apiId: Int): ServerResponse {
        return apiwish.getWishGames(apiId).await()
    }
}