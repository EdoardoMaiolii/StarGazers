package com.example.stargazers.models

import com.google.gson.JsonArray
import com.google.gson.JsonObject

data class ErrorBody(val message : String, val code : String, val text: String)

sealed class APIResponse<X>{

    data class Success<X>(val data: JsonObject) : APIResponse<X>()

    data class Error<X>(val error: ErrorBody):APIResponse<X>()

}

sealed class APIResponseJsonArray<T>{

    data class Success<T>(val data: JsonArray) : APIResponseJsonArray<T>()

    data class Error<T>(val error: ErrorBody): APIResponseJsonArray<T>()

}