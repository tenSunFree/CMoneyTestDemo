package com.home.cmoneytestdemo.common.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import java.lang.reflect.ParameterizedType

@Suppress("DEPRECATION")
abstract class BaseActivity<VM : ViewModel, VDB : ViewDataBinding> : AppCompatActivity() {

    lateinit var viewModel: VM
    lateinit var binding: VDB

    @get:LayoutRes
    protected abstract val layoutId: Int

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
        // get the actual type of a generic parameter
        val vmClass =
            (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VM>
        viewModel = ViewModelProviders.of(this).get(vmClass)
        onActivityCreated()
    }

    protected abstract fun onActivityCreated()

    fun jumpActivity(context: Context, clazz: Class<Any>) {
        val intent = Intent()
        intent.setClass(context, clazz)
        startActivity(intent)
    }
}