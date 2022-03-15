package ru.gb.weathers.view.details

import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import ru.gb.weathers.BuildConfig
import ru.gb.weathers.model.WeatherDTO
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors

@RequiresApi(Build.VERSION_CODES.N)
class WeatherLoader(
    private val listener: WeatherLoaderListener,
    private val lat: Double,
    private val lon: Double,
) {

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadWeather() =
        try {
            val uri =
                URL("GET https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=${lon}")
            val handler = Handler()

            Thread {
                lateinit var urlConnection: HttpURLConnection
                try {
                    urlConnection = (uri.openConnection() as HttpURLConnection).apply {
                        requestMethod = "GET"
                        readTimeout = 10000
                        addRequestProperty(
                            "yandex_weather_api_key",
                            BuildConfig.WEATHER_API_KEY
                        )
                    }

                    val bufferedReader =
                        BufferedReader(InputStreamReader(urlConnection.inputStream))

                    val response = getLines(bufferedReader)

                    val weatherDTO: WeatherDTO =
                        Gson().fromJson(response, WeatherDTO::class.java)

                    handler.post {
                        listener.onLoaded(weatherDTO)
                    }

                } catch (e: Exception) {
                    Log.e("", "Fail connection", e)
                    e.printStackTrace()
                    listener.onFailed(e)
                } finally {
                    urlConnection.disconnect()
                }
            }.start()
        } catch (e: MalformedURLException) {
            Log.e("", "Fail URI", e)
            e.printStackTrace()
            listener.onFailed(e)
        }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }

    interface WeatherLoaderListener {
        fun onLoaded(weatherDTO: WeatherDTO)
        fun onFailed(throwable: Throwable)
    }
}