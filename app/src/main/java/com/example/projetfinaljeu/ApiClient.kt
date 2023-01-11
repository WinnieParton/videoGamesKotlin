package com.example.projetfinaljeu

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

   /* suspend fun getGames(): ServerResponse {
        return api.getGames().await()
    }*/
}