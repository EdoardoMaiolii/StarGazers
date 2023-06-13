package com.example.stargazers

import com.example.stargazers.http.NetworkConfig
import com.example.stargazers.http.NetworkRetrofit
import com.example.stargazers.services.StarGazersService
import org.koin.dsl.module

val appModule = module {

    single {
        NetworkRetrofit(
            NetworkConfig(BuildConfig.API_BASE_URL)
        ).retrofit.create(StarGazersService::class.java)
    }
}