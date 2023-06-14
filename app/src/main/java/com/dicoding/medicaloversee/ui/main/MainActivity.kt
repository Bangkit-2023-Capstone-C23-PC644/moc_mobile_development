package com.dicoding.medicaloversee.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.medicaloversee.R
import com.dicoding.medicaloversee.databinding.ActivityMainBinding
import com.dicoding.medicaloversee.model.UserPreference
import com.dicoding.medicaloversee.ui.ViewModelFactory
import com.dicoding.medicaloversee.ui.onboard.OnboardActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        navView.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED

        val navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController()

        navController?.let { setupBottomNavigation(navView, it) }

        userPreference = UserPreference.getInstance(applicationContext.dataStore)

        setupViewModel()
        observeViewModel()

    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(userPreference)
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, OnboardActivity::class.java))
                finish()
            }
        }
    }

    private fun observeViewModel() {
        mainViewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        mainViewModel.isError.observe(this) {
            Toast.makeText(this, "Failed to load story", Toast.LENGTH_SHORT).show()
        }

        mainViewModel.isSuccess.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

    }

    private fun setupBottomNavigation(navView: BottomNavigationView, navController: NavController) {
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->

            if (destination.id == R.id.navigation_home || destination.id == R.id.navigation_maps ||
                destination.id == R.id.navigation_bookmark || destination.id == R.id.navigation_profile
            ) {
                navView.visibility = View.VISIBLE
            } else {
                navView.visibility = View.GONE
            }
        }
    }
}