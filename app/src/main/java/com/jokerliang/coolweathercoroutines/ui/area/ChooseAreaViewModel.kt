package com.jokerliang.coolweathercoroutines.ui.area

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.jokerliang.coolweathercoroutines.base.BaseViewModel
import com.jokerliang.coolweathercoroutines.data.model.place.City
import com.jokerliang.coolweathercoroutines.data.model.place.County
import com.jokerliang.coolweathercoroutines.data.model.place.Province
import com.jokerliang.coolweathercoroutines.data.PlaceRepository
import com.jokerliang.coolweathercoroutines.ui.area.ChooseAreaViewModel.ChooseAreaUiModel.Companion.defaultWeatherId
import com.jokerliang.coolweathercoroutines.ui.area.ChooseAreaFragment.Companion.LEVEL_CITY
import com.jokerliang.coolweathercoroutines.ui.area.ChooseAreaFragment.Companion.LEVEL_COUNTY
import com.jokerliang.coolweathercoroutines.ui.area.ChooseAreaFragment.Companion.LEVEL_PROVINCE

/**
求贤若饥 虚心若愚
 * @author jokerliang
 */
class ChooseAreaViewModel: BaseViewModel() {
    private val repository = PlaceRepository.instance

    var currentLevel = LEVEL_PROVINCE


    val dataList = MutableLiveData<List<String>>()

    init {
        dataList.value = ArrayList<String>()
    }

    val uiState = MutableLiveData<ChooseAreaUiModel>()

    private var provinceList = ArrayList<Province>()
    private var cityList = ArrayList<City>()
    private var countiesList = ArrayList<County>()
    private var upClickProvinces: Province? = null

    var clickPosition = 0
        set(value) {
            when(currentLevel) {
                LEVEL_PROVINCE -> queryCity(value)
                LEVEL_CITY -> queryCounties(value)
                LEVEL_COUNTY -> clickCounties(value)
            }
            field = value
        }

    fun queryProvinces() {
        Log.d("joker", "queryProvinces$repository")
        currentLevel = LEVEL_PROVINCE
        emitUi(backButtonVisibility = false, isLoading = true)
        launch {
            provinceList = repository.fetchProvinces() as ArrayList<Province>
            dataList.value = provinceList.map { it.provinceName }
        }
    }

    fun queryCity(position: Int = -999) {
        if (position != -999) {
            upClickProvinces = provinceList[position]
        }
        currentLevel = LEVEL_CITY
        emitUi(upClickProvinces!!.provinceName, isLoading = true)
        launch {
            cityList = repository.fetchCity(upClickProvinces!!.provinceCode) as ArrayList<City>
            dataList.value = cityList.map { it.cityName }
        }
    }

    private fun queryCounties(position: Int) {
        val clickCity = cityList[position]
        currentLevel = LEVEL_COUNTY
        emitUi(clickCity.cityName, isLoading = true)
        launch {
            countiesList = repository.fetchCounties(clickCity.provinceId, clickCity.cityCode) as ArrayList<County>
            dataList.value = countiesList.map { it.countyName }
        }
    }

    /**
     * 点击到了县一级，进入天气界面
     */
    private fun clickCounties(position: Int) {
        val clickCounty = countiesList[position]
        emitUi(weatherId = clickCounty.weatherId)
    }


    fun setEnterFlag(weatherId: String) {
        repository.setEnterFlag(weatherId)
    }


    private fun emitUi(title: String = "中国"
                       , weatherId: String = defaultWeatherId,
                       backButtonVisibility: Boolean = true,
                       isLoading: Boolean = false) {
        uiState.value = ChooseAreaUiModel(title, weatherId, backButtonVisibility, isLoading)
    }



    data class ChooseAreaUiModel(
        val title: String,
        val weatherId: String,
        val backButtonVisibility: Boolean,
        val isLoading: Boolean
    ) {
        fun clickCounty(): Boolean {
            return weatherId != defaultWeatherId
        }

        companion object {
            const val defaultWeatherId = "-999999"
        }
    }

}