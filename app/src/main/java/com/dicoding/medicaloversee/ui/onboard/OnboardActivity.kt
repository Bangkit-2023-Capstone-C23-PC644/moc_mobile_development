package com.dicoding.medicaloversee.ui.onboard

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.medicaloversee.R
import com.dicoding.medicaloversee.model.UserPreference
import com.dicoding.medicaloversee.ui.ViewModelFactory
import com.dicoding.medicaloversee.ui.login.LoginActivity
import com.dicoding.medicaloversee.ui.main.MainActivity
import com.dicoding.medicaloversee.ui.main.MainViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class OnboardActivity : AppCompatActivity() {

    private lateinit var button: Button
    private lateinit var mainViewModel: MainViewModel
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboard)

        button = findViewById(R.id.button)
        button.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        userPreference = UserPreference.getInstance(applicationContext.dataStore)

        setupViewModel()

    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(userPreference)
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this) { user ->
            if (user != null && user.isLogin) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}