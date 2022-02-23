package ru.gb.weathers.viewModel

import ru.gb.weathers.model.Weather

sealed class AppState{
    data class Start(val weatherHome: Weather) : AppState()
    data class Success(val weatherData: List<Weather>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()

}
