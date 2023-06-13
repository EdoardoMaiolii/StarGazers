package com.example.stargazers.http

import android.util.Log
import com.example.stargazers.models.APIResponse
import com.example.stargazers.models.APIResponseJsonArray
import com.example.stargazers.models.ErrorBody
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import retrofit2.Response
import java.net.UnknownHostException

inline fun <reified T> responseHandlerJsonArray(apiCall: () -> Response<T>, gson: Gson) : APIResponseJsonArray<T> {
    val result = runCatching (apiCall)
    if(result.isSuccess) {
        val response = result.getOrNull()!!
        return if (response.isSuccessful) {
            if (response.body() is JsonArray) {
                val responseJsonArray : JsonArray = response.body() as JsonArray
                Log.i("APIresponse", "Success")
                APIResponseJsonArray.Success(responseJsonArray)
            } else {
                if (response.body() != null) {
                    APIResponseJsonArray.Error(ErrorBody("Undefined Response Body ${response.body()!!::class.java}","400","Type of Response Body is not JsonArray"))
                } else {
                    APIResponseJsonArray.Error(ErrorBody("Undefined Response Body","400","Type of Response Body is not JsonArray"))
                }
            }

        } else{
            APIResponseJsonArray.Error(ErrorBody("message","400","OkHttpResponse was not successful"))
        }
    }else{
        val exception = result.exceptionOrNull()
        exception?.printStackTrace()
        return when (exception) {
            is HttpException -> {
                val code = exception.code()
                APIResponseJsonArray.Error(ErrorBody("Http Exception",code.toString(),"Check Your Internet Connection"))
            }
            is UnknownHostException ->{
                APIResponseJsonArray.Error(ErrorBody(exception.message ?: "message","400","Check Your Internet Connection"))
            }
            is JsonSyntaxException -> {
                APIResponseJsonArray.Error(ErrorBody(exception.message ?: "error message","400","Errore nel parsing del Json"))
            }
            else ->
                APIResponseJsonArray.Error(ErrorBody(exception?.message ?: "Generic error","400", exception.toString()))
        }
    }
}

inline fun <reified X> responseHandler(apiCall: () -> Response<X>, gson: Gson) : APIResponse<X> {
    val result = runCatching (apiCall)
    if(result.isSuccess) {
        val response = result.getOrNull()!!
        return if (response.isSuccessful) {
            val httpResponse = response as Response<JsonObject>
            if(httpResponse.body()?.has("error") == true) {
                val loginResponse =
                    gson.fromJson(httpResponse.body()?.get("error"), ErrorBody::class.java)
                Log.i("APIresponse", "ErrorResponse $loginResponse")
                APIResponse.Error(loginResponse)
            }else{
                Log.i("APIresponse", "Success")
                APIResponse.Success(httpResponse.body()!!)
            }
        } else{
            APIResponse.Error(ErrorBody("message","400","Generic Error"))
        }
    }else{
        val exception = result.exceptionOrNull()
        exception?.printStackTrace()
        return when (exception) {
            is HttpException -> {
                val code = exception.code()
                APIResponse.Error(ErrorBody("Http Exception",code.toString(),"Check Your Internet Connection"))
            }
            is UnknownHostException ->{
                APIResponse.Error(ErrorBody(exception.message ?: "message","400","Check Your Internet Connection"))
            }
            else ->
                APIResponse.Error(ErrorBody("message","400","Generic Error Exception"))
        }
    }
}
