package com.example.projetfinaljeu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
    val gameList = MutableLiveData<List<Game>>()

    fun setGame(list: List<Game>) {
        gameList.value = list
    }

    fun getGame()=gameList
}