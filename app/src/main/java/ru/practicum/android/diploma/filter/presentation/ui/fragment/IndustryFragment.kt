package ru.practicum.android.diploma.filter.presentation.ui.fragment

import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentIndustryBinding
import ru.practicum.android.diploma.filter.domain.model.Industry
import ru.practicum.android.diploma.filter.presentation.model.IndustriesState
import ru.practicum.android.diploma.filter.presentation.ui.adapter.IndustryAdapter
import ru.practicum.android.diploma.filter.presentation.viewmodel.IndustryViewModel
import ru.practicum.android.diploma.util.Constants
import ru.practicum.android.diploma.util.hide
import ru.practicum.android.diploma.util.show

class IndustryFragment : BaseFragment<FragmentIndustryBinding, IndustryViewModel>(
    inflate = FragmentIndustryBinding::inflate
) {
    override val viewModel: IndustryViewModel by viewModel()

    private val adapter by lazy {
        IndustryAdapter { industry ->
            viewModel.selectIndustry(industry)
        }
    }

    override fun initViews(): Unit = with(binding) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = this@IndustryFragment.adapter
        }

        setupSearchTextWatcher()
        viewModel.loadIndustries()
        setupClickListeners()
    }

    private fun setupClickListeners() = with(binding) {
        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        btnClear.setOnClickListener {
            binding.edittextSearch.text?.clear()
        }

        chooseButton.setOnClickListener {
            viewModel.saveSelectedIndustryToFilters()
            findNavController().popBackStack()
        }
    }

    override fun subscribe() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            renderState(state)
        }

        viewModel.selectedIndustry.observe(viewLifecycleOwner) { selectedIndustry ->
            renderSelectButton(selectedIndustry)
            adapter.setSelectedIndustry(selectedIndustry?.id)
        }

        viewModel.filteredIndustries.observe(viewLifecycleOwner) { industries ->
            adapter.saveData(industries)
        }
    }

    private fun renderState(state: IndustriesState) {
        when (state) {
            IndustriesState.Loading -> showLoadingState()
            IndustriesState.NetworkError -> showNetworkError()
            IndustriesState.ServerError -> showServerError()
            is IndustriesState.Success -> showSuccessState(state.industriesList)
            IndustriesState.NothingFound -> showNothingFoundError()
        }
    }

    private fun showNothingFoundError() {
        showPlaceholder(
            placeholderDrawable = R.drawable.placeholder_not_found,
            textRes = R.string.error_cant_get_list
        )
    }

    private fun showNetworkError() {
        showPlaceholder(
            placeholderDrawable = R.drawable.placeholder_network_error,
            textRes = R.string.error_no_internet
        )
    }

    private fun showServerError() {
        showPlaceholder(
            placeholderDrawable = R.drawable.placeholder_server_error_search,
            textRes = R.string.error_server
        )
    }

    private fun renderSelectButton(industry: Industry?) = with(binding) {
        if (industry == null || viewModel.uiState.value !is IndustriesState.Success) {
            chooseButton.hide()
            chooseButton.isEnabled = false
        } else {
            chooseButton.show()
            chooseButton.isEnabled = true
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

    private fun setupSearchTextWatcher() = with(binding) {
        edittextSearch.addTextChangedListener { text ->
            updateClearButtonIcon(text)
            viewModel.updateSearchQuery(text?.toString() ?: Constants.EMPTY_STRING)
        }
    }

    private fun updateClearButtonIcon(text: CharSequence?) = with(binding) {
        btnClear.setImageResource(
            if (text?.isNotEmpty() == true) R.drawable.edit_text_clear_button else R.drawable.ic_search
        )
    }
}
