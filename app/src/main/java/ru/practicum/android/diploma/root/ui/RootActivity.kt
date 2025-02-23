package ru.practicum.android.diploma.root.ui

import androidx.core.bundle.Bundle
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.BaseActivity
import ru.practicum.android.diploma.databinding.ActivityRootBinding

class RootActivity : BaseActivity<ActivityRootBinding>(ActivityRootBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
    }

    override fun initViews() {
        bottomNavigationManager()
    }

    private fun bottomNavigationManager() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.favoritesFragment -> showBottomNavigation()
                R.id.teamFragment -> showBottomNavigation()
                R.id.searchFragment -> showBottomNavigation()
                else -> hideBottomNavigation()
            }
        }
    }

    private fun hideBottomNavigation() {
        binding.bottomNavigationView.isVisible = false
        binding.divider.isVisible = false
    }

    private fun showBottomNavigation() {
        binding.bottomNavigationView.isVisible = true
        binding.divider.isVisible = true
    }
}
