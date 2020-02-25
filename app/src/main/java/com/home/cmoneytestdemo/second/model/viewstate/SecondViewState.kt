package com.home.cmoneytestdemo.second.model.viewstate

import com.home.cmoneytestdemo.second.model.viewdata.SecondViewData

sealed class SecondViewState {

    object Loading : SecondViewState()

    data class Success(
        val postsData: SecondViewData.PostsData? = null,
        val response: String? = null
    ) : SecondViewState()

    data class Failure(val throwable: String?) : SecondViewState()
}

