package com.example.stargazers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stargazers.models.ErrorBody

open class ErrorViewModel : ViewModel(){
    protected val mErrorModel = MutableLiveData<ErrorBody>()
    val errorModel: LiveData<ErrorBody> = mErrorModel

}