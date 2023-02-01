package com.example.projetfinaljeu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    //val game = MutableLiveData<MutableList<Game>>(mutableListOf())

    val game = MutableLiveData<List<Game>>()

    public fun setData(list: List<Game>) {
        game.value = list
    }

    @JvmName("getGame1")
    public fun getGame() = game



}