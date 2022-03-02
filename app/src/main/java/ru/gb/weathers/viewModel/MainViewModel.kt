package ru.gb.weathers.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gb.weathers.model.Repository
import ru.gb.weathers.model.RepositoryImp
import java.lang.Thread.sleep


class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImp: Repository = RepositoryImp()
) : ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun getWeatherHomeCity() = getDataHomeCity()

    fun getWeatherFromLocalSourceRus() = getDataFromLocalSource(isRussian = true)
    fun getWeatherFromLocalSourceWorld() = getDataFromLocalSource(isRussian = false)
    fun getWeatherFromRemoteSource() = getDataFromLocalSource(isRussian = true)

    private fun getDataFromLocalSource(isRussian: Boolean) {
        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(1000)
            liveDataToObserve.postValue(
                AppState.Success(
                    if (isRussian)
                        repositoryImp.getWeatherFromLocalStorageRus()
                    else
                        repositoryImp.getWeatherFromLocalStorageWorld()
                )
            )
        }.start()
    }

    private fun getDataHomeCity() =
        liveDataToObserve.postValue(AppState.Start(repositoryImp.getHomeWeather()))
}