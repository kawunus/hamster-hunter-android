package ru.practicum.android.diploma.team.presentation.ui.fragment

import androidx.fragment.app.viewModels
import ru.practicum.android.diploma.core.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentTeamBinding
import ru.practicum.android.diploma.team.presentation.viewmodel.TeamViewModel

class TeamFragment : BaseFragment<FragmentTeamBinding, TeamViewModel>(
    inflate = FragmentTeamBinding::inflate
) {
    override val viewModel: TeamViewModel by viewModels()

    override fun initViews() {

        // TODO: init views
    }

    override fun subscribe() {

        // TODO: subscribe on viewModel
    }
}
