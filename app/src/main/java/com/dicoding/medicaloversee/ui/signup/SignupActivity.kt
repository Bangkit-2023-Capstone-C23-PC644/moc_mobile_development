package com.dicoding.medicaloversee.ui.signup

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.medicaloversee.R
import com.dicoding.medicaloversee.databinding.ActivitySignupBinding
import com.dicoding.medicaloversee.model.UserPreference
import com.dicoding.medicaloversee.ui.ViewModelFactory
import com.dicoding.medicaloversee.ui.login.LoginActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var signupViewModel: SignupViewModel

    private var location: Location? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        binding.chkAddLocation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                getLastLocation()
            } else {
                this.location = null
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setupViewModel()
        observeViewModel()
        setupAction()
        playAnimation()
    }

    private fun setupViewModel() {
        signupViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[SignupViewModel::class.java]
    }

    private fun observeViewModel() {
        signupViewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        signupViewModel.isError.observe(this) {
            Toast.makeText(this, "Failed to register", Toast.LENGTH_SHORT).show()
        }

        signupViewModel.isSuccess.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val nik = binding.edRegisterNik.text.toString()
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val phone = binding.edRegisterPhone.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            val lintang = location?.latitude.toString()
            val bujur = location?.longitude.toString()
            signupViewModel.registerUser(nik, name, email, phone, password, lintang, bujur)
        }
        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun playAnimation() {

        val nik = ObjectAnimator.ofFloat(binding.nikTextView, View.ALPHA, 1f).setDuration(500)
        val nikEdit = ObjectAnimator.ofFloat(binding.nikEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val name = ObjectAnimator.ofFloat(binding.namaTextView, View.ALPHA, 1f).setDuration(500)
        val nameEdit = ObjectAnimator.ofFloat(binding.namaEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val phone = ObjectAnimator.ofFloat(binding.phoneTextView, View.ALPHA, 1f).setDuration(500)
        val phoneEdit = ObjectAnimator.ofFloat(binding.phoneEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEdit = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEdit = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val chkLocation = ObjectAnimator.ofFloat(binding.chkAddLocation, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)
        val loginLink = ObjectAnimator.ofFloat(binding.loginStatement, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(nik, nikEdit, name, nameEdit, phone, phoneEdit, email, emailEdit, password, passwordEdit, chkLocation, signup, loginLink)
            startDelay = 500
        }.start()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, "Tidak mendapatkan permission.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun getLastLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    this.location = location

                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.please_activate_location_message),
                        Toast.LENGTH_SHORT
                    ).show()

                    binding.chkAddLocation.isChecked = false
                }
            }
        } else {

            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->

        when {
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                getLastLocation()
            }
            else -> {
                Snackbar
                    .make(
                        binding.root,
                        getString(R.string.location_permission_denied),
                        Snackbar.LENGTH_SHORT
                    )
                    .setActionTextColor(getColor(R.color.white))
                    .setAction(getString(R.string.location_permission_denied_action)) {

                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also { intent ->
                            val uri = Uri.fromParts("package", packageName, null)
                            intent.data = uri

                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                    .show()

                binding.chkAddLocation.isChecked = false
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}