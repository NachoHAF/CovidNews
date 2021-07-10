package com.upsa.covidnews.api

import com.upsa.covidnews.model.NewsResponse
import com.upsa.covidnews.model.SpainResponse
import com.upsa.covidnews.model.WorldResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiNews {
    @GET("v2/top-headlines?q=covid&language=es&apiKey=c5594fd51bc5456497e5f086390a9f04")
    fun getNews(): Call<NewsResponse>
}