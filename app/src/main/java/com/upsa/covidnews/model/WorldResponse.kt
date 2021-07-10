package com.upsa.covidnews.model

data class WorldResponse(
    val cases : Int,
    val deaths : Int,
    val recovered : Int,
    val active : Int,
    val critical : Int
)
