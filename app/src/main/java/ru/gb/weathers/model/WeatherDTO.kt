package ru.gb.weathers.model

data class WeatherDTO(
    val fact: FactDTO?
)

data class FactDTO(
    val temp: Int?,
    val feelsLike: Int?,
    val condition: String?
)
