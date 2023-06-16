package com.example.stargazers.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.stargazers.ErrorViewModel
import com.example.stargazers.Event
import com.example.stargazers.http.responseHandlerJsonArray
import com.example.stargazers.models.APIResponseJsonArray
import com.example.stargazers.models.User
import com.example.stargazers.repositories.StarGazersRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.lang.reflect.Type

class HomeListUsersViewModel(private val starGazersRepository: StarGazersRepository, private val gson: Gson): ErrorViewModel() {

    private val _users = MutableLiveData<Event<ArrayList<User>>>()
    var users : LiveData<Event<ArrayList<User>>> = _users

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private var nextPage = 1

    fun initLoading() {
        _loading.postValue(true)
    }

    fun getNextPage(): Int {
        return nextPage
    }

    fun setNextPage(nextPage: Int) {
        this.nextPage = nextPage
    }

    fun getStarGazersList(ownerName: String, repositoryName: String, page: Int?, limit: Int?) {
        _loading.postValue(true)
        viewModelScope.launch {
            val repositoryResponse =
                responseHandlerJsonArray ({starGazersRepository.getListOfStarGazers(ownerName, repositoryName,
                    page ?: nextPage, limit)}, gson)
            _loading.postValue(false)
            when(repositoryResponse) {
                is APIResponseJsonArray.Error -> {
                    mErrorModel.postValue(repositoryResponse.error)
                }
                is APIResponseJsonArray.Success -> {
                    val listType: Type = object : TypeToken<ArrayList<User>>() {}.type
                    val usersList: ArrayList<User> =  gson.fromJson(repositoryResponse.data, listType)
                    nextPage++
                    _users.postValue(Event(usersList))
                }
            }
        }
    }
}