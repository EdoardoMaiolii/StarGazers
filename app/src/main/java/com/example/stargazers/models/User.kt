package com.example.stargazers.models

import com.google.gson.annotations.SerializedName

class User(
    @SerializedName("login")
    val name: String,
    val id: Long,
    val avatar_url: String,
)