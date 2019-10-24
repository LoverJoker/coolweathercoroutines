package com.jokerliang.coolweathercoroutines

import android.app.Application
import android.content.Context
import org.litepal.LitePal
import kotlin.properties.Delegates

/**
求贤若饥 虚心若愚
 * @author jokerliang
 */
class App: Application() {

    companion object {
        var CONTEXT: Context by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        CONTEXT = applicationContext
        LitePal.initialize(this)
    }
}