package com.example.stargazers

import com.example.stargazers.http.NetworkConfig
import com.example.stargazers.http.NetworkRetrofit
import com.example.stargazers.repositories.StarGazersRepository
import com.example.stargazers.services.StarGazersService
import com.example.stargazers.ui.HomeListUsersViewModel
import com.google.gson.Gson
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        Gson()
    }

    single {
        NetworkRetrofit(
            NetworkConfig(BuildConfig.API_BASE_URL)
        ).retrofit.create(StarGazersService::class.java)
    }

    single {
        StarGazersRepository(get(), get())
    }

    viewModel{HomeListUsersViewModel(get(), get())}
}