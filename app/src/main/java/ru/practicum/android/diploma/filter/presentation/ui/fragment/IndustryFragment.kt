package ru.practicum.android.diploma.filter.presentation.ui.fragment

import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentIndustryBinding
import ru.practicum.android.diploma.filter.domain.model.Country
import ru.practicum.android.diploma.filter.domain.model.Industry
import ru.practicum.android.diploma.filter.presentation.model.IndustriesState
import ru.practicum.android.diploma.filter.presentation.ui.adapter.IndustryAdapter
import ru.practicum.android.diploma.filter.presentation.viewmodel.IndustryViewModel
import ru.practicum.android.diploma.util.hide
import ru.practicum.android.diploma.util.show

class IndustryFragment : BaseFragment<FragmentIndustryBinding, IndustryViewModel>(
    inflate = FragmentIndustryBinding::inflate
) {
    override val viewModel: IndustryViewModel by viewModel()

    private val adapter by lazy {
        IndustryAdapter {
        }
    }

    override fun initViews(): Unit = with(binding) {
        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = this@IndustryFragment.adapter
        }

        viewModel.loadIndustries()
    }

    override fun subscribe() = with(binding) {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            renderState(state)
        }
    }

    private fun renderState(state: IndustriesState) {
        when (state) {
            IndustriesState.Loading -> showLoadingState()
            IndustriesState.NetworkError -> showPlaceholder(
                placeholderDrawable = R.drawable.placeholder_network_error,
                textRes = R.string.error_no_internet
            )

            IndustriesState.ServerError -> showPlaceholder(
                placeholderDrawable = R.drawable.placeholder_server_error_search,
                textRes = R.string.error_server
            )

            is IndustriesState.Success -> showSuccessState(state.industriesList)
        }
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

    private fun showLoadingState() = with(binding) {
        hideAllViews()
        progressBar.show()
    }

    private fun showSuccessState(industriesList: List<Industry>) = with(binding) {
        hideAllViews()
        recyclerView.show()
        adapter.saveData(industriesList)
    }

    private fun hideAllViews() = with(binding) {
        progressBar.hide()
        recyclerView.hide()
        llErrorContainer.hide()
    }
}
