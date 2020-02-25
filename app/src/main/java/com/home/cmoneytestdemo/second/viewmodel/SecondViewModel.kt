package com.home.cmoneytestdemo.second.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.home.cmoneytestdemo.second.model.repository.SecondRepository
import com.home.cmoneytestdemo.second.model.viewdata.SecondViewData
import com.home.cmoneytestdemo.second.model.viewstate.SecondViewState
import kotlinx.coroutines.launch

class SecondViewModel : ViewModel() {

    private val repository = SecondRepository()
    private val viewStateLiveData: MutableLiveData<SecondViewState> = MutableLiveData()

    fun getData() {
        viewModelScope.launch {
            repository.subscribe = {
                when (it) {
                    is SecondViewState.Loading -> {
                        viewStateLiveData.postValue(SecondViewState.Loading)
                    }
                    is SecondViewState.Success -> {
                        val postsData = SecondViewData.PostsData(it.response)
                        viewStateLiveData.postValue(SecondViewState.Success(postsData = postsData))
                    }
                    is SecondViewState.Failure -> {
                        viewStateLiveData.postValue(SecondViewState.Failure(it.throwable))
                    }
                }
            }
            repository.getData()
        }
    }

    fun getViewState(): LiveData<SecondViewState> {
        return viewStateLiveData
    }
}
