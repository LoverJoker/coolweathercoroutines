package com.jokerliang.coolweathercoroutines.data.model.weather


import com.google.gson.annotations.SerializedName

class HeWeather {

    @SerializedName("HeWeather")
    var weather: List<Weather>? = null

}