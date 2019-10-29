package com.jokerliang.coolweathercoroutines.ui.main

import android.content.Intent
import com.jokerliang.coolweathercoroutines.R
import com.jokerliang.coolweathercoroutines.base.BaseVMActivity
import com.jokerliang.coolweathercoroutines.ui.weather.WeatherActivity

class MainActivity : BaseVMActivity<MainViewModel>() {

    override fun providerVMClass(): Class<MainViewModel>? = MainViewModel::class.java

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initView() {

    }

    override fun initData() {
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            if (!isFirstEnter()) {
                val intent = Intent(this@MainActivity, WeatherActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    companion object {
        // 请求天气API的Key，请到http://guolin.tech/api/weather/register申请免费的Key
        const val KEY = "45dd25f63300445e967b461d2037e4f9"
    }



}
