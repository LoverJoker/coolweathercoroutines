package com.jokerliang.coolweathercoroutines.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
    求贤若饥 虚心若愚
 * @author jokerliang
 */
open class BaseRepository {
    suspend fun <T> request(block: suspend () -> T): T {
        return withContext(Dispatchers.IO) {
            block()
        }
    }
}