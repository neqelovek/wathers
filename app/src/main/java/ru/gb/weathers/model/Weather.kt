package ru.gb.weathers.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weather(
    val city: City = getDefaultCity(),
    val dayTemperature: Int = 10,
    val nightTemperature: Int = 5
) : Parcelable

fun getDefaultCity() = City(city = "Воронеж", lat = 51.672, lon = 39.1843)

fun getWorldCities() = listOf(
    Weather(City("Лондон", 51.5085300, -0.1257400), 1, 2),
    Weather(City("Токио", 35.6895000, 139.6917100), 3, 4),
    Weather(City("Париж", 48.8534100, 2.3488000), 5, 6),
    Weather(City("Берлин", 52.52000659999999, 13.404953999999975), 7, 8),
    Weather(City("Рим", 41.9027835, 12.496365500000024), 9, 10),
    Weather(City("Минск", 53.90453979999999, 27.561524400000053), 11, 12),
)


fun getRussianCities() = listOf(
    Weather(City("Москва", 55.755826, 37.617299900000035), 1, 2),
    Weather(City("Санкт-Петербург", 59.9342802, 30.335098600000038), 3, 3),
    Weather(City("Новосибирск", 55.00835259999999, 82.93573270000002), 5, 6),
    Weather(City("Екатеринбург", 56.83892609999999, 60.60570250000001), 7, 8),
    Weather(City("Нижний Новгород", 56.2965039, 43.936059), 9, 10),
    Weather(City("Казань", 55.8304307, 49.06608060000008), 11, 12),
)


