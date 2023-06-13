package com.example.stargazers.repositories

import com.example.stargazers.services.StarGazersService
import com.google.gson.Gson
import com.google.gson.JsonArray
import retrofit2.Response

class StarGazersRepository(
    private val starGazersService: StarGazersService,
    private val gson: Gson
) {
    suspend fun getListOfStarGazers(ownerName: String, repositoryName: String, page: Int?, limit: Int?) : Response<JsonArray>{
        return starGazersService.getListOfStarGazers(ownerName, repositoryName,page, limit)
    }
}