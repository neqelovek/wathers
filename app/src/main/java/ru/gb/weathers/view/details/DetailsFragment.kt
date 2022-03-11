package ru.gb.weathers.view.details

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import ru.gb.weathers.R
import ru.gb.weathers.databinding.FragmentDetailsBinding
import ru.gb.weathers.model.Weather
import ru.gb.weathers.model.WeatherDTO


class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var weatherBundle: Weather

    private val onLoadListener: WeatherLoader.WeatherLoaderListener =
        object : WeatherLoader.WeatherLoaderListener {

            override fun onLoaded(weatherDTO: WeatherDTO) {
                displayWeather(weatherDTO)

            }

            override fun onFailed(throwable: Throwable) {

            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weatherBundle = arguments?.getParcelable<Weather>(BUNDLE_EXTRA) ?: Weather()
        with(binding) {
            mainViewDetailsFragment.visibility = View.GONE
            loadingLayoutDetailsFragment.visibility = View.VISIBLE
        }
        val loader =
            WeatherLoader(onLoadListener, weatherBundle.city.lat, weatherBundle.city.lon)
        loader.loadWeather()

    }

    private fun displayWeather(weatherDTO: WeatherDTO) {
        with(binding) {
            mainViewDetailsFragment.visibility = View.GONE
            loadingLayoutDetailsFragment.visibility = View.VISIBLE

            val city = weatherBundle.city

            cityNameDetailsFragment.text = city.city
            cityCoordinatesDetailsFragment.text = String.format(
                getString(R.string.city_coordinates),
                city.lat.toString(),
                city.lon.toString()
            )

            weatherCondition.text = weatherDTO.fact?.condition
//            if (weatherDTO.fact?.daytime == "d") {
//                temperatureDaysDetailsFragment.text = weatherDTO.fact?.temp.toString()
//            } else (weatherDTO.fact?.daytime == "n")
//            temperatureNightDetailsFragment.text = weatherDTO.fact?.temp.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val BUNDLE_EXTRA = "weather"

        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}