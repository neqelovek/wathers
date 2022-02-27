package ru.gb.weathers.model

class RepositoryImp : Repository {
    override fun getWeatherFromServer() = Weather()
    override fun getHomeWeather() = Weather()
    override fun getWeatherFromLocalStorageRus() = getRussianCities()
    override fun getWeatherFromLocalStorageWorld() = getWorldCities()
}