package com.example.projetfinaljeu

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("GetMostPlayedGames/v1/?")
    fun getGames(): Deferred<ServerResponse>
}