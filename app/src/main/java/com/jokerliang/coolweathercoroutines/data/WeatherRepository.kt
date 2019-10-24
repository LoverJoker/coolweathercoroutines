package com.jokerliang.coolweathercoroutines.data

import com.jokerliang.coolweathercoroutines.base.BaseRepository
import com.jokerliang.coolweathercoroutines.data.network.ServiceClient
import com.jokerliang.coolweathercoroutines.data.network.api.WeatherService
import com.jokerliang.coolweathercoroutines.utils.Preference

/**
求贤若饥 虚心若愚
 * @author jokerliang
 */
class WeatherRepository private constructor(): BaseRepository() {

    companion object {
        val instance by lazy { WeatherRepository() }
    }
    private val service by lazy { ServiceClient.create(WeatherService::class.java) }
    private var isFirst by Preference(Preference.IS_FIRST, "")

    suspend fun fetchWeather(weatherId: String, key: String) = request{ service.getWeather(weatherId, key) }

    suspend fun fetchBackground(): String = request {
        val bingPckResponseBody = service.getBingPck()
        val string = bingPckResponseBody.string()
        string
    }

    /**
     * 判断是否是第一次进入
     */
    fun isFirstEnter():Boolean = isFirst == ""

    fun getWeatherIdCache(): String {
        return isFirst
    }
}