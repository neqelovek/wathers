package ru.gb.weathers.view

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.BoringLayout.make
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.make
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.main_fragment.*
import ru.gb.weathers.R
import ru.gb.weathers.databinding.MainFragmentBinding
import ru.gb.weathers.model.Weather
import ru.gb.weathers.view.details.DetailsFragment
import ru.gb.weathers.viewModel.AppState
import ru.gb.weathers.viewModel.MainViewModel


class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private var isDataSetRus: Boolean = true

    companion object {
        fun newInstance() = MainFragment()
    }

    private val adapter = MainFragmentAdapter(object : MainFragmentAdapter.OnItemViewClickListener {
        override fun onItemViewClick(weather: Weather) {
            activity?.supportFragmentManager?.apply {
                beginTransaction()
                    .add(R.id.container, DetailsFragment.newInstance(Bundle().apply {
                        putParcelable(DetailsFragment.BUNDLE_EXTRA, weather)
                    }))
                    .addToBackStack("")
                    .commitAllowingStateLoss()
            }
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainFragmentRecyclerView.adapter = adapter
        binding.buttonCity.setOnClickListener { chengWeatherDataSet() }

        viewModel.apply {
            val observer = Observer<AppState> { renderData(it) }
            getLiveData().observe(viewLifecycleOwner, observer)
            getWeatherFromLocalSourceRus()

            val observer2 = Observer<AppState> { renderData(it) }
            getLiveData().observe(viewLifecycleOwner, observer2)
            getWeatherHomeCity()
        }
    }

    private fun chengWeatherDataSet() =
        if (isDataSetRus) {
            viewModel.getWeatherFromLocalSourceWorld()
            binding.buttonCity.setText(R.string.rus)
        } else {
            viewModel.getWeatherFromLocalSourceRus()
            binding.buttonCity.setText(R.string.world)
        }.also { isDataSetRus = !isDataSetRus }

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
                binding.citySearch.setEndIconOnClickListener{
                    city_search.showKeyboard()
                    city_search.hideKeyboard()
                }
            }
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                adapter.setWeather(appState.weatherData)
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE

                binding.main.showSnackbar(
                    getString(R.string.error),
                    getString(R.string.reload),
                    { viewModel.getWeatherFromLocalSourceRus() }
                )
            }
        }
    }

    private fun View.showSnackbar(
        text: String,
        actionText: String,
        action: (View) -> Unit,
        length: Int = Snackbar.LENGTH_INDEFINITE
    ) {
        make(this, text, length).setAction(actionText, action).show()
    }

    private fun setDataHomeCity(weatherHome: Weather) {
        binding.apply {
            cityHome.text = weatherHome.city.city

            cityHomeCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                weatherHome.city.lat.toString(),
                weatherHome.city.lon.toString()
            )
            textCHTemperatureDay.text = weatherHome.dayTemperature.toString()
            textCHTemperatureNight.text = weatherHome.nightTemperature.toString()
        }
    }

    fun View.showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        this.requestFocus()
        imm.showSoftInput(this, 0)
    }

    fun View.hideKeyboard(): Boolean {
        try {
            val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
        } catch (ignored: RuntimeException) { }
        return false
    }
}