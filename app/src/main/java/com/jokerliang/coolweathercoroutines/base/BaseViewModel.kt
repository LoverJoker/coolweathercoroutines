package com.jokerliang.coolweathercoroutines.base

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
求贤若饥 虚心若愚
 * @author jokerliang
 */
open class BaseViewModel: ViewModel(), LifecycleObserver {

    val mException: MutableLiveData<Throwable> = MutableLiveData()

    fun <T> launch(block: suspend CoroutineScope.() -> T) {
        viewModelScope.launch {
            tryCatch(block, {})
        }
    }

    private suspend fun <T> tryCatch(
        tryBlock: suspend CoroutineScope.() -> T,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit
    ) {
        coroutineScope {
            try {
                tryBlock()
            } catch (e: Throwable) {
                mException.value = e
                catchBlock(e)
            }
        }

    }
}