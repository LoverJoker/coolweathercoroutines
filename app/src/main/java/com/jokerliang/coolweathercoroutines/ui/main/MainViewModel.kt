package com.jokerliang.coolweathercoroutines.ui.main

import android.util.Log
import com.jokerliang.coolweathercoroutines.base.BaseViewModel
import com.jokerliang.coolweathercoroutines.data.PlaceRepository

/**
求贤若饥 虚心若愚
 * @author jokerliang
 */
class MainViewModel: BaseViewModel() {

    private val repository = PlaceRepository.instance

    /**
     * 标识是否是第一次进入
     */
    fun isFirstEnter():Boolean {
        Log.d("joker", "mainViewModel$repository")
        return repository.isFirstEnter()
    }


}