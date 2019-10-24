package com.jokerliang.coolweathercoroutines.base

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

/**
    求贤若饥 虚心若愚
 * @author jokerliang
 */
abstract class BaseVMActivity<VM: BaseViewModel>: AppCompatActivity(), LifecycleObserver {

    lateinit var mViewModel: VM
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = window.decorView
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.statusBarColor = Color.TRANSPARENT
        }
        initViewModel()
        startObserve()
        setContentView(getLayoutId())
        initView()
        initData()
    }

    private fun initViewModel() {
        providerVMClass()?.let {
            mViewModel = ViewModelProvider(this).get(it)
            mViewModel.let(lifecycle::addObserver)
        }
    }

    open fun providerVMClass(): Class<VM>? = null

    open fun startObserve() {
        mViewModel.mException.observe(this, Observer {
            it?.let {
                onError(it)
            }
        })
    }

    open fun onError(e: Throwable) {}

    override fun onDestroy() {
        mViewModel.let {
            lifecycle.removeObserver(it)
        }
        super.onDestroy()
    }

    abstract fun getLayoutId(): Int
    abstract fun initView()
    abstract fun initData()
}