package com.home.cmoneytestdemo.second.view.activity

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.home.cmoneytestdemo.R
import com.home.cmoneytestdemo.common.base.BaseActivity
import com.home.cmoneytestdemo.common.extension.toast
import com.home.cmoneytestdemo.databinding.ActivitySecondBinding
import com.home.cmoneytestdemo.second.model.viewdata.SecondViewData
import com.home.cmoneytestdemo.second.model.viewstate.SecondViewState
import com.home.cmoneytestdemo.second.view.adapter.SecondAdapter
import com.home.cmoneytestdemo.second.viewmodel.SecondViewModel

class SecondActivity : BaseActivity<SecondViewModel, ActivitySecondBinding>() {

    override val layoutId: Int
        get() = R.layout.activity_second

    override fun onActivityCreated() {
        setToolbar()
        setViewModel()
    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setViewModel() {
        viewModel.getViewState().observe(this, Observer { setViewState(it) })
        viewModel.getData()
    }

    private fun setViewState(viewState: SecondViewState) {
        when (viewState) {
            is SecondViewState.Loading -> setProgress(true)
            is SecondViewState.Success -> {
                setProgress(false)
                showSuccess(viewState.postsData)
            }
            is SecondViewState.Failure -> {
                setProgress(false)
                showError(viewState.throwable)
            }
        }
    }

    private fun setProgress(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showSuccess(data: SecondViewData.PostsData?) {
        binding.recyclerView.apply {
            val adapter = SecondAdapter()
            adapter.setResponse(data!!.response)
            adapter.setOnItemClickListener = { toast(it) }
            this.adapter = adapter
            val spanCount = 4
            layoutManager = GridLayoutManager(context, spanCount)
            val cacheSize = -1
            // Solve the problem of reuse disorder
            setItemViewCacheSize(cacheSize)
        }
    }

    private fun showError(message: String?) {
        toast(message!!)
    }
}
