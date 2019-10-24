package com.jokerliang.coolweathercoroutines.data

import com.jokerliang.coolweathercoroutines.base.BaseRepository
import com.jokerliang.coolweathercoroutines.data.model.place.City
import com.jokerliang.coolweathercoroutines.data.model.place.County
import com.jokerliang.coolweathercoroutines.data.model.place.Province
import com.jokerliang.coolweathercoroutines.data.network.ServiceClient
import com.jokerliang.coolweathercoroutines.data.network.api.PlaceService
import com.jokerliang.coolweathercoroutines.data.network.dao.PlaceDao
import com.jokerliang.coolweathercoroutines.utils.Preference

/**
    求贤若饥 虚心若愚
 * @author jokerliang
 */
class PlaceRepository private constructor(): BaseRepository() {
    companion object {
        val instance by lazy { PlaceRepository() }
    }

    private val service by lazy { ServiceClient.create(PlaceService::class.java) }
    private val dao by lazy { PlaceDao() }
    private var isFirst by Preference(Preference.IS_FIRST, "")

    suspend fun fetchProvinces(): List<Province> {
        val provinceList = dao.getProvince()
        if (provinceList.isEmpty()) {
            val provinces = service.getProvinces()
            dao.saveProvince(provinces)
            return provinces
        }
        return provinceList
    }

    suspend fun fetchCity(provinceId: Int): List<City> {
        val cityList = dao.getCityList(provinceId)
        if (cityList.isEmpty()) {
            val cities = service.getCities(provinceId)
            cities.forEach { it.provinceId = provinceId }
            dao.saveCityList(cities)
            return cities
        }
        return cityList
    }

    suspend fun fetchCounties(provinceId: Int, cityId: Int): List<County> {
        val countyList = dao.getCountyList(cityId)
        if (countyList.isEmpty()) {
            val counties = service.getCounties(provinceId, cityId)
            counties.forEach { it.cityId = cityId }
            dao.saveCountyList(counties)
            return counties
        }
        return countyList

    }



    /**
     * 判断是否是第一次进入
     */
    fun isFirstEnter():Boolean = isFirst == ""

    fun setEnterFlag(weatherId: String) {
        isFirst = weatherId
    }

}