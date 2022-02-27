package ru.gb.weathers.model

class RepositoryImp : Repository {
    override fun getWeatherFromServer(): Weather {
        return Weather()
    }

    override fun getHomeWeather(): Weather {
        return Weather()
    }

    override fun getWeatherFromLocalStorageRus(): List<Weather> {
        return getRussianCities()
    }

    override fun getWeatherFromLocalStorageWorld(): List<Weather> {
        return getWorldCities()
    }
}