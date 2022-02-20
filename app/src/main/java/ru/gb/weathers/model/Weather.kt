package ru.gb.weathers.model

data class Weather(
    val city: City = getDefaultCity(),
    val dayTemperature: Int = 10,
    val nightTemperature: Int = 5
)

fun getDefaultCity() = City(city = "Воронеж", lat = 51.672, lon = 39.1843)
