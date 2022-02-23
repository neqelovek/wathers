package ru.gb.weathers.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import ru.gb.weathers.R
import ru.gb.weathers.databinding.MainFragmentBinding
import ru.gb.weathers.model.Weather
import ru.gb.weathers.view.details.DetailsFragment
import ru.gb.weathers.viewModel.AppState
import ru.gb.weathers.viewModel.MainViewModel


class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private var isDataSetRus: Boolean = true


    companion object {
        fun newInstance() = MainFragment()
    }

    private val adapter = MainFragmentAdapter(object : MainFragmentAdapter.OnItemViewClickListener {
        override fun onItemViewClick(weather: Weather) {
            val manager = activity?.supportFragmentManager

            if (manager != null) {
                val bundle = Bundle()
                bundle.putParcelable(DetailsFragment.BUNDLE_EXTRA, weather)
                manager.beginTransaction()
                    .add(R.id.container, DetailsFragment.newInstance(bundle))
                    .addToBackStack("")
                    .commitAllowingStateLoss()
            }
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainFragmentRecyclerView.adapter = adapter
        binding.mainFragmentFAB.setOnClickListener { chengWeatherDataSet() }

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val observer = Observer<AppState> { renderData(it) }
        val observer2 = Observer<AppState> { renderData(it) }

        viewModel.getLiveData().observe(viewLifecycleOwner, observer)
        viewModel.getWeatherFromLocalSourceRus()

        viewModel.getLiveData().observe(viewLifecycleOwner, observer2)
        viewModel.getWeatherHomeCity()

    }

    private fun chengWeatherDataSet() {
        if(isDataSetRus){
            viewModel.getWeatherFromLocalSourceWorld()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_earth)
        }else{
            viewModel.getWeatherFromLocalSourceRus()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_russia)
        }
        isDataSetRus =!isDataSetRus
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter.removeListener()
        _binding = null
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Start -> {
                val weatherData = appState.weatherHome
                setDataHomeCity(weatherData)
            }

            is AppState.Success -> {
                val weatherData = appState.weatherData
                binding.loadingLayout.visibility = View.GONE
                adapter.setWeather(appState.weatherData)

            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE

                Snackbar
                    .make(binding.mainFragmentFAB, getString(R.string.error), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.reload)) {
                        viewModel.getWeatherFromLocalSourceRus()
                    }
                    .show()
            }
        }
    }

    private fun setDataHomeCity(weatherHome: Weather) {
        binding.cityHome.text = weatherHome.city.city
        binding.cityHomeCoordinates.text = String.format(
            getString(R.string.city_coordinates),
            weatherHome.city.lat.toString(),
            weatherHome.city.lon.toString()
        )
        binding.textCHTemperatureDay.text = weatherHome.dayTemperature.toString()
        binding.textCHTemperatureNight.text = weatherHome.nightTemperature.toString()
    }
}