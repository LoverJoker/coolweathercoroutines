package com.jokerliang.coolweathercoroutines.data.network.api

import com.jokerliang.coolweathercoroutines.data.model.place.City
import com.jokerliang.coolweathercoroutines.data.model.place.County
import com.jokerliang.coolweathercoroutines.data.model.place.Province
import retrofit2.http.GET
import retrofit2.http.Path

interface PlaceService {

    @GET("api/china")
    suspend fun getProvinces(): ArrayList<Province>

    @GET("api/china/{provinceId}")
    suspend fun getCities(@Path("provinceId") provinceId: Int): ArrayList<City>

    @GET("api/china/{provinceId}/{cityId}")
    suspend fun getCounties(@Path("provinceId") provinceId: Int, @Path("cityId") cityId: Int): ArrayList<County>

}