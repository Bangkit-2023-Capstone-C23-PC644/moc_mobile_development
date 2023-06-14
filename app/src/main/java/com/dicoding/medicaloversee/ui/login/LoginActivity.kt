package com.dicoding.medicaloversee.ui.login

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
import com.dicoding.medicaloversee.databinding.ActivityLoginBinding
import com.dicoding.medicaloversee.model.UserModel
import com.dicoding.medicaloversee.model.UserPreference
import com.dicoding.medicaloversee.ui.ViewModelFactory
import com.dicoding.medicaloversee.ui.main.MainActivity
import com.dicoding.medicaloversee.ui.signup.SignupActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var user: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        observeViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]

        loginViewModel.getUser().observe(this) { user ->
            this.user = user
        }
    }

    private fun observeViewModel() {
        loginViewModel.isLoading.observe(this) {
            binding.progressBar1.visibility = if (it) View.VISIBLE else View.GONE
        }

        loginViewModel.isError.observe(this) {
            Toast.makeText(this, "Failed to login", Toast.LENGTH_SHORT).show()
        }

        loginViewModel.isSuccess.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val nik = binding.edLoginNik.text.toString()
            val password = binding.edLoginPassword.text.toString()
            loginViewModel.loginUser(nik, password)
        }

        binding.signupButton.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    @Deprecated("Deprecated in Java", ReplaceWith("finishAffinity()"))
    override fun onBackPressed() {
        finishAffinity()
    }
}