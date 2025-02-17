package ru.practicum.android.diploma.team.presentation.ui.fragment

import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentTeamBinding
import ru.practicum.android.diploma.team.presentation.viewmodel.TeamViewModel

class TeamFragment(
    override val viewModel: TeamViewModel
) : BaseFragment<FragmentTeamBinding, TeamViewModel>(
    inflate = FragmentTeamBinding::inflate
) {
    override fun initViews() {
        TODO("Not yet implemented")
    }

    override fun subscribe() {
        TODO("Not yet implemented")
    }
}
