package com.upsa.covidnews.model

data class SpainResponse (
    val cases : Int,
    val deaths : Int,
    val recovered : Int,
    val active : Int,
    val critical : Int
)