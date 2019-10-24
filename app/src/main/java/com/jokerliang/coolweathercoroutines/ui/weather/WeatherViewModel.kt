package com.jokerliang.coolweathercoroutines.ui.weather

import androidx.lifecycle.MutableLiveData
import com.jokerliang.coolweathercoroutines.base.BaseViewModel
import com.jokerliang.coolweathercoroutines.data.WeatherRepository
import com.jokerliang.coolweathercoroutines.data.model.weather.Weather
import com.jokerliang.coolweathercoroutines.ui.main.MainActivity

/**
    求贤若饥 虚心若愚
 * @author jokerliang
 */
class WeatherViewModel: BaseViewModel() {
    private val repository = WeatherRepository.instance
    val uiState = MutableLiveData<WeatherUiModel>()

    val weather = MutableLiveData<Weather>()

    val background = MutableLiveData<String>()

    fun fetchWeather(weatherId: String) {
        launch{
            val fetchWeather = repository.fetchWeather(weatherId, MainActivity.KEY)
            weather.value = fetchWeather.weather!![0]
        }
    }

    fun fetchBackground() {
        launch {
            background.value = repository.fetchBackground()
        }
    }

    private fun showLoading() {
        emitUiState(true)
    }

    private fun emitUiState(showProgress: Boolean = false) {
        uiState.value = WeatherUiModel(showProgress)
    }


    fun getWeatherIdCache(): String {
        return repository.getWeatherIdCache()
    }
    data class WeatherUiModel(
        val showProgress: Boolean
    )
}