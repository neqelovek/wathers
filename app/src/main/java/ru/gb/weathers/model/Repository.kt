package ru.gb.weathers.model

interface Repository {
    fun getWeatherFromServer(): Weather
    fun getHomeWeather(): Weather
    fun getWeatherFromLocalStorageRus(): List<Weather>
    fun getWeatherFromLocalStorageWorld(): List<Weather>

}