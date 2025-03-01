package ru.practicum.android.diploma.filter.presentation.ui.fragment

import android.os.Bundle
import androidx.core.bundle.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentRegionBinding
import ru.practicum.android.diploma.filter.presentation.ui.adapter.RegionsAdapter
import ru.practicum.android.diploma.filter.presentation.viewmodel.RegionScreenState
import ru.practicum.android.diploma.filter.presentation.viewmodel.RegionViewModel

class RegionFragment : BaseFragment<FragmentRegionBinding, RegionViewModel>(
    inflate = FragmentRegionBinding::inflate
) {
    override val viewModel: RegionViewModel by viewModel()
    private val regionsAdapter = RegionsAdapter()
    private var countryId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgumentsData()
    }

    override fun initViews() {
        setupRecyclerView()
        setupClickListeners()
        setupSearchTextWatcher()
        loadInitialData()
    }

    override fun subscribe() {
        observeRegions()
        observeScreenState()
    }

    // получаем ID страны из сохраненных фильтров
    private fun getArgumentsData() {
        countryId = viewModel.getParentId()
    }

    // настраиваем RecyclerView и его адаптер
    private fun setupRecyclerView() {
        binding.recycler.apply {
            adapter = regionsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        setupRegionClickListener()
    }

    //  обрабатываем клик по региону в списке
    private fun setupRegionClickListener() {
        regionsAdapter.onItemClick = { region ->
            viewModel.saveSelectedRegion(region)
            setFragmentResult(
                REQUEST_KEY_REGION,
                bundleOf(
                    BUNDLE_KEY_REGION_ID to region.id,
                    BUNDLE_KEY_REGION_NAME to region.name,
                    BUNDLE_KEY_PARENT_ID to region.parentId
                )
            )
            findNavController().popBackStack()
        }
    }

    // настраиваем обработчики кликов
    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnClear.setOnClickListener {
            binding.edittextSearch.text?.clear()
        }
    }

    // настройка отслеживания изменений текста
    private fun setupSearchTextWatcher() {
        binding.edittextSearch.addTextChangedListener(
            onTextChanged = { text, _, _, _ ->
                updateClearButtonIcon(text)
                viewModel.updateSearchQuery(text?.toString() ?: "")
            },
        )
    }

    // обновление иконки кнопки
    private fun updateClearButtonIcon(text: CharSequence?) {
        binding.btnClear.setImageResource(
            if (text?.isNotEmpty() == true) {
                R.drawable.edit_text_clear_button
            } else {
                R.drawable.ic_search
            }
        )
    }

    // загружаем список регионов при старте
    private fun loadInitialData() {

        viewModel.loadRegions(countryId ?: "")

    }

    // подписываемся на обновления списка регионов
    private fun observeRegions() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.filteredRegions.collect { regions ->
                regionsAdapter.setData(regions)
                binding.apply {
                    when {
                        edittextSearch.text.isNullOrEmpty() -> {
                            // Показываем полный список при пустом поиске
                            llErrorContainer.isVisible = false
                            recycler.isVisible = true
                        }

                        regions.isEmpty() -> {
                            // Показываем ошибку поиска
                            llErrorContainer.isVisible = true
                            recycler.isVisible = false
                            ivErrorImage.setImageResource(R.drawable.placeholder_not_found)
                            tvErrorText.setText(R.string.not_region)
                        }

                        else -> {
                            // Показываем результаты поиска
                            llErrorContainer.isVisible = false
                            recycler.isVisible = true
                        }
                    }
                }
            }
        }
    }

    // подписываемся на обновления на progressBar
    private fun observeScreenState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.screenState.collect { state ->
                updateScreenState(state)
            }
        }
    }

    // управляет видимостью views в зависимости от наличия данных
    private fun updateScreenState(state: RegionScreenState) {
        binding.apply {
            when (state) {
                RegionScreenState.Loading -> {
                    progressBar.isVisible = true
                    llErrorContainer.isVisible = false
                    recycler.isVisible = false
                }

                RegionScreenState.Content -> {
                    progressBar.isVisible = false
                    llErrorContainer.isVisible = false
                    recycler.isVisible = true
                }

                is RegionScreenState.Error.NetworkError -> {
                    progressBar.isVisible = false
                    llErrorContainer.isVisible = true
                    recycler.isVisible = false
                    ivErrorImage.setImageResource(R.drawable.placeholder_network_error)
                    tvErrorText.setText(R.string.error_no_internet)
                }

                is RegionScreenState.Error.ServerError -> {
                    progressBar.isVisible = false
                    llErrorContainer.isVisible = true
                    recycler.isVisible = false
                    ivErrorImage.setImageResource(R.drawable.placeholder_not_found_regions)
                    tvErrorText.setText(R.string.error_nothing_found)
                }
            }
        }
    }

    companion object {
        const val REQUEST_KEY_REGION = "request_key_region" // ключ для передачи результата выбора региона
        const val BUNDLE_KEY_REGION_ID = "bundle_key_region_id" // ключ для ID выбранного региона
        const val BUNDLE_KEY_REGION_NAME = "bundle_key_region_name" // ключ для названия выбранного региона
        const val BUNDLE_KEY_PARENT_ID = "bundle_key_parent_id" // ключ для ID родительской страны
        const val COUNTRY_ID_KEY = "country_id" // ключ для получения ID страны из аргументов
    }
}
