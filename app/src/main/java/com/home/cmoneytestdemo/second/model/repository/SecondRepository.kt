package com.home.cmoneytestdemo.second.model.repository

import com.home.cmoneytestdemo.common.AppConstants
import com.home.cmoneytestdemo.common.util.HttpUtil
import com.home.cmoneytestdemo.second.model.viewstate.SecondViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SecondRepository {

    var subscribe: ((viewState: SecondViewState) -> Unit)? = null

    suspend fun getData() = withContext(Dispatchers.IO) {
        val url = AppConstants.BASE_URL + "/photos"
        subscribe?.invoke(SecondViewState.Loading)
        HttpUtil.getData(url, object : HttpUtil.Subscribe {
            override fun onSuccess(response: String) {
                subscribe?.invoke(SecondViewState.Success(response = response))
            }

            override fun onError(e: Exception) {
                subscribe?.invoke(SecondViewState.Failure(e.toString()))
            }
        })
    }
}