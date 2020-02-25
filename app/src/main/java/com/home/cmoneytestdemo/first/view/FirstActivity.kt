package com.home.cmoneytestdemo.first.view

import com.home.cmoneytestdemo.R
import com.home.cmoneytestdemo.common.base.BaseActivity
import com.home.cmoneytestdemo.common.extension.click
import com.home.cmoneytestdemo.databinding.ActivityFirstBinding
import com.home.cmoneytestdemo.first.viewmodel.FirstViewModel
import com.home.cmoneytestdemo.second.view.activity.SecondActivity

class FirstActivity : BaseActivity<FirstViewModel, ActivityFirstBinding>() {

    override val layoutId: Int
        get() = R.layout.activity_first

    override fun onActivityCreated() {
        setClickListener()
    }

    private fun setClickListener() {
        binding.textIewRequestApi.click {
            jumpActivity(this, SecondActivity().javaClass)
        }
    }
}
