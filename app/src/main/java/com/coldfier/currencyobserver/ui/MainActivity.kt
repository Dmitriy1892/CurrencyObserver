package com.coldfier.currencyobserver.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.coldfier.currencyobserver.App
import com.coldfier.currencyobserver.R
import com.coldfier.currencyobserver.databinding.ActivityMainBinding
import com.coldfier.currencyobserver.di.ViewModelFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    private val navController: NavController by lazy {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (applicationContext as App).appComponent.inject(this)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavView = binding.bottomNavigation
        bottomNavView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val titleRes = when (destination.id) {
                R.id.popularFragment -> R.string.popular
                R.id.favoritesFragment -> R.string.favorites
                else -> R.string.sort
            }
            binding.tvScreenTitle.text = getString(titleRes)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}