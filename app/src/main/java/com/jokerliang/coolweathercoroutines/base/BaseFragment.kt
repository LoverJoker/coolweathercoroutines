package com.jokerliang.coolweathercoroutines.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

/**
求贤若饥 虚心若愚
 * @author jokerliang
 */
abstract class BaseFragment<VM: BaseViewModel>: Fragment() {
    lateinit var mViewModel: VM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutResId(), container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewModel()
        initView()
        initData()
        startObserve()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initViewModel() {
        providerVMClass()?.let {
            mViewModel = ViewModelProvider(this).get(it)
            //Todo 这里需要仔细研究下
            mViewModel.let(lifecycle::addObserver)
        }
    }



    open fun startObserve() {
        //Todo 这里可能有mViewModel 未初始化异常
        mViewModel.mException.observe(this, Observer { it?.let { onError(it) } })
    }

    open fun onError(e: Throwable) {}


    abstract fun getLayoutResId(): Int

    abstract fun initView()

    abstract fun initData()

    open fun providerVMClass(): Class<VM>? = null

    override fun onDestroy() {
        mViewModel.let {
            lifecycle.removeObserver(it)
        }
        super.onDestroy()
    }


}