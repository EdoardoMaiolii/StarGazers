package com.example.stargazers.services

import com.google.gson.JsonArray
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StarGazersService {

    @GET("repos/{ownerName}/{repositoryName}/stargazers")
    suspend fun getListOfStarGazers(
        @Path("ownerName") ownerName: String,
        @Path("repositoryName") repositoryName: String,
        @Query("page") page: Int?,
        @Query("per_page") limit: Int?,
    ): Response<JsonArray>
}