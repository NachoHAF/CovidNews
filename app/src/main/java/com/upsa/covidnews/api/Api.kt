package com.upsa.covidnews.api

import com.upsa.covidnews.model.SpainResponse
import com.upsa.covidnews.model.WorldResponse
import retrofit2.Call
import retrofit2.http.GET

interface Api {
    @GET("countries/spain")
    fun getSpain(): Call<SpainResponse>

    @GET("all")
    fun getAll(): Call<WorldResponse>
}