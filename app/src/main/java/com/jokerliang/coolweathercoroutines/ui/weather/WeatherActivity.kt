package com.jokerliang.coolweathercoroutines.ui.weather

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.jokerliang.coolweathercoroutines.R
import com.jokerliang.coolweathercoroutines.base.BaseVMActivity
import com.jokerliang.coolweathercoroutines.data.model.weather.Weather
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.aqi.*
import kotlinx.android.synthetic.main.forecast.*
import kotlinx.android.synthetic.main.now.*
import kotlinx.android.synthetic.main.suggestion.*
import kotlinx.android.synthetic.main.title.*

/**
求贤若饥 虚心若愚
 * @author jokerliang
 */
class WeatherActivity: BaseVMActivity<WeatherViewModel>() {
    private lateinit var mWeatherId: String

    override fun getLayoutId(): Int = R.layout.activity_weather
    override fun providerVMClass(): Class<WeatherViewModel>? = WeatherViewModel::class.java

    override fun initView() {
        mWeatherId = mViewModel.getWeatherIdCache()
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            refreshWeather(mWeatherId)
            swipeRefresh.isRefreshing = false
        }
        navButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
            // (chooseAreaFragment as ChooseAreaFragment).queryProvinces()
        }
    }

    override fun initData() {
        refreshWeather(mWeatherId)
        mViewModel.fetchBackground()
    }

    fun refreshWeather(weatherId: String) {
        mWeatherId = weatherId
        mViewModel.run {
            fetchWeather(weatherId)
        }
    }

    override fun startObserve() {
        super.startObserve()
        mViewModel.apply {
            uiState.observe(this@WeatherActivity, Observer {

            })

            weather.observe(this@WeatherActivity, Observer{
                swipeRefresh.isRefreshing = false
                showWeatherInfo(it)
            })

            background.observe(this@WeatherActivity, Observer {
                Glide.with(this@WeatherActivity).load(it).into(bingPicImg)
            })

        }
    }

    override fun onError(e: Throwable) {
        Toast.makeText(this@WeatherActivity, "是不是没网了", Toast.LENGTH_SHORT).show()
    }


    /**
     * 这段是完全Copy郭神的
     */
    private fun showWeatherInfo(weather: Weather) {
        val cityName = weather.basic.cityName
        val updateTime = weather.basic.update.updateTime.split(" ")[1]
        val degree = weather.now.temperature + "℃"
        val weatherInfo = weather.now.more.info
        titleCity.text = cityName
        titleUpdateTime.text = updateTime
        degreeText.text = degree
        weatherInfoText.text = weatherInfo
        forecastLayout.removeAllViews()
        for (forecast in weather.forecastList) {
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false)
            val dateText = view.findViewById(R.id.dateText) as TextView
            val infoText = view.findViewById(R.id.infoText) as TextView
            val maxText = view.findViewById(R.id.maxText) as TextView
            val minText = view.findViewById(R.id.minText) as TextView
            dateText.text = forecast.date
            infoText.text = forecast.more.info
            maxText.text = forecast.temperature.max
            minText.text = forecast.temperature.min
            forecastLayout.addView(view)
        }
        aqiText.text = weather.aqi.city.aqi
        pm25Text.text = weather.aqi.city.pm25
        val comfort = "舒适度：" + weather.suggestion.comfort.info
        val carWash = "洗车指数：" + weather.suggestion.carWash.info
        val sport = "运动建议：" + weather.suggestion.sport.info
        comfortText.text = comfort
        carWashText.text = carWash
        sportText.text = sport
        weatherLayout.visibility = View.VISIBLE
    }
}