package com.jokerliang.coolweathercoroutines.data.network.dao

import com.jokerliang.coolweathercoroutines.base.BaseDao
import com.jokerliang.coolweathercoroutines.data.model.place.City
import com.jokerliang.coolweathercoroutines.data.model.place.County
import com.jokerliang.coolweathercoroutines.data.model.place.Province
import org.litepal.LitePal

/**
求贤若饥 虚心若愚
 * @author jokerliang
 */
class PlaceDao: BaseDao() {
    suspend fun saveProvince(provinces: ArrayList<Province>?): ArrayList<Province>?{
        if (provinces != null && provinces.isNotEmpty()) {
            ioHandler {
                LitePal.saveAll(provinces)
            }
        }
        return provinces
    }

    suspend fun saveCityList(cityList: ArrayList<City>?): ArrayList<City>? {
        if (cityList != null && cityList.isNotEmpty()) {
            ioHandler {
                LitePal.saveAll(cityList)
            }
        }
        return cityList
    }

    suspend fun saveCountyList(countyList: ArrayList<County>?): ArrayList<County>? {
        if (countyList != null && countyList.isNotEmpty()) {
            ioHandler {
                LitePal.saveAll(countyList)
            }
        }
        return countyList
    }



    suspend fun getProvince(): MutableList<Province> = ioHandler { LitePal.findAll(Province::class.java) }

    suspend fun getCityList(provinceId: Int): MutableList<City>
            = ioHandler { LitePal.where("provinceId = ?", provinceId.toString()).find(City::class.java) }

    suspend fun getCountyList(cityId: Int): MutableList<County>
            = ioHandler { LitePal.where("cityId = ?", cityId.toString()).find(County::class.java) }


}