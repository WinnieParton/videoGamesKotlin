package com.example.projetfinaljeu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
    val dataList = MutableLiveData<List<Game>>()

    fun setData(list: List<Game>) {
        dataList.value = list
        println("1111111111111111  "+ list.size )

    }

    fun getData() = dataList
}