package com.upsa.covidnews.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://disease.sh/v3/covid-19/"
    private const val BASE_URL2 = "https://newsapi.org/"
    val instance: Api by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(Api::class.java)
    }

    val instance2: ApiNews by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL2)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiNews::class.java)
    }
}