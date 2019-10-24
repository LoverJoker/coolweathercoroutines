package com.jokerliang.coolweathercoroutines.data.network.api

import com.jokerliang.coolweathercoroutines.data.model.weather.HeWeather
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("api/weather")
    suspend fun getWeather(@Query("cityid") weatherId: String, @Query("key") key: String): HeWeather

    @GET("api/bing_pic")
    suspend fun getBingPck(): ResponseBody

}