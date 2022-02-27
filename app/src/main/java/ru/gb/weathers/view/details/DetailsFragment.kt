package ru.gb.weathers.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.gb.weathers.R
import ru.gb.weathers.databinding.FragmentDetailsBinding
import ru.gb.weathers.model.Weather


class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getParcelable<Weather>(BUNDLE_EXTRA)?.let {
            it.city.also {
                binding.cityNameDetailsFragment.text = it.city

                binding.cityCoordinatesDetailsFragment.text = String.format(
                    getString(R.string.city_coordinates),
                    it.lat.toString(),
                    it.lon.toString()
                )
            }
            binding.temperatureDaysDetailsFragment.text = it.dayTemperature.toString()
            binding.temperatureNightDetailsFragment.text = it.nightTemperature.toString()
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