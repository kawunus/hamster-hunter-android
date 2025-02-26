package ru.practicum.android.diploma.root.ui

import androidx.core.bundle.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.BaseActivity
import ru.practicum.android.diploma.databinding.ActivityRootBinding
import ru.practicum.android.diploma.util.hide
import ru.practicum.android.diploma.util.show

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
        binding.bottomNavigationView.hide()
        binding.divider.hide()
    }

    private fun showBottomNavigation() {
        binding.bottomNavigationView.show()
        binding.divider.show()
    }
}
