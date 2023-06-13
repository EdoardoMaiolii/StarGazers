package com.example.stargazers.repositories

import android.content.Context
import com.example.stargazers.services.StarGazersService
import com.google.gson.Gson

class StarGazersRepository(
    private val starGazersService: StarGazersService,
    private val context: Context,
    private val gson: Gson
) {
    suspend fun getListOfStarGazers(ownerName: String, repositoryName: String) {

    }
}