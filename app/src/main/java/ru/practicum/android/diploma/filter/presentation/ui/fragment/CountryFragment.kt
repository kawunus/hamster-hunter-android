package ru.practicum.android.diploma.filter.presentation.ui.fragment

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentCountryBinding
import ru.practicum.android.diploma.filter.domain.model.Country
import ru.practicum.android.diploma.filter.presentation.model.CountriesState
import ru.practicum.android.diploma.filter.presentation.ui.adapter.CountryAdapter
import ru.practicum.android.diploma.filter.presentation.viewmodel.CountryViewModel
import ru.practicum.android.diploma.util.hide
import ru.practicum.android.diploma.util.show

class CountryFragment : BaseFragment<FragmentCountryBinding, CountryViewModel>(
    FragmentCountryBinding::inflate
) {
    override val viewModel: CountryViewModel by viewModel()

    private val adapter by lazy {
        CountryAdapter { country ->
            viewModel.onCountrySelected(country)
            findNavController().navigateUp()
        }
    }

    override fun initViews() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = this@CountryFragment.adapter
        }

        viewModel.loadCountries()
    }

    override fun subscribe() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is CountriesState.Loading -> showLoadingState()
                is CountriesState.Success -> showSuccessState(state.countries)
                is CountriesState.NetworkError -> showPlaceholder(
                    placeholderDrawable = R.drawable.placeholder_network_error,
                    textRes = R.string.error_no_internet
                )

                is CountriesState.ServerError -> showPlaceholder(
                    placeholderDrawable = R.drawable.placeholder_server_error_search,
                    textRes = R.string.error_server
                )
            }
        }
    }

    private fun showLoadingState() {
        hideAllViews()
        binding.progressBar.show()
    }

    private fun showSuccessState(countries: List<Country>) {
        hideAllViews()
        binding.recycler.show()
        adapter.setData(countries)
    }

    private fun showPlaceholder(
        placeholderDrawable: Int,
        textRes: Int
    ) {
        hideAllViews()
        binding.llErrorContainer.show()
        binding.ivErrorImage.setImageResource(placeholderDrawable)
        binding.tvErrorText.setText(textRes)
    }

    private fun hideAllViews() {
        binding.progressBar.hide()
        binding.recycler.hide()
        binding.llErrorContainer.hide()
    }
}

