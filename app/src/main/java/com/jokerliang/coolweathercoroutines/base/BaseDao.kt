package com.jokerliang.coolweathercoroutines.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
求贤若饥 虚心若愚
 * @author jokerliang
 * @date 2019-10-23 16:57
 */
open class BaseDao {
    suspend fun <T> ioHandler(block: suspend () -> T): T {
        return withContext(Dispatchers.IO) {
            block()
        }
    }
}