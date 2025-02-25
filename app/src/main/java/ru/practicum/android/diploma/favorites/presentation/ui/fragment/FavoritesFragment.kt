package ru.practicum.android.diploma.favorites.presentation.ui.fragment

import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentFavoritesBinding
import ru.practicum.android.diploma.favorites.presentation.model.FavoritesState
import ru.practicum.android.diploma.favorites.presentation.ui.adapter.VacancyAdapter
import ru.practicum.android.diploma.favorites.presentation.viewmodel.FavoritesViewModel
import ru.practicum.android.diploma.search.domain.model.Vacancy

class FavoritesFragment : BaseFragment<FragmentFavoritesBinding, FavoritesViewModel>(
    inflate = FragmentFavoritesBinding::inflate
) {
    override val viewModel: FavoritesViewModel by viewModel()

    private var isClickAllowed = true

    private val adapter = VacancyAdapter { vacancy ->
        if (clickDebounce()) {
            findNavController().navigate(
                FavoritesFragmentDirections
                    .actionFavoritesFragmentToVacancyFragment(vacancy.id)
            )
        }
    }

    override fun initViews() {
        isClickAllowed = true
        initRecyclerView()
        viewModel.getData()
    }

    override fun subscribe() {
        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            render(state)
        }
    }

    private fun render(state: FavoritesState) {
        when (state) {
            is FavoritesState.Content -> renderContent(state.vacanciesList)
            FavoritesState.Empty -> renderEmptyState()
            FavoritesState.Loading -> renderLoadingState()
        }
    }

    private fun renderContent(vacanciesList: List<Vacancy>) = with(binding) {
        errorMessageTextView.hide()
        placeholderImageView.hide()
        progressBar.hide()
        recyclerView.show()
        adapter.saveData(vacanciesList)
    }

    private fun renderLoadingState() = with(binding) {
        recyclerView.hide()
        errorMessageTextView.hide()
        placeholderImageView.hide()
        progressBar.show()
    }

    private fun renderEmptyState() = with(binding) {
        recyclerView.hide()
        progressBar.hide()

        errorMessageTextView.apply {
            show()
            text = getString(R.string.error_favorites_is_empty)
        }

        placeholderImageView.apply {
            show()
            setImageResource(R.drawable.placeholder_favorites_is_empty)
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            adapter = this@FavoritesFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    override fun onResume() {
        super.onResume()
        viewModel.getData()
    }

    private companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private fun View.show() {
        isVisible = true
    }

    private fun View.hide() {
        isVisible = false
    }
}
