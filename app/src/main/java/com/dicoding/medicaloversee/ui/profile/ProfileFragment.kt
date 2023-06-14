package com.dicoding.medicaloversee.ui.profile

import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.medicaloversee.databinding.FragmentProfileBinding
import com.dicoding.medicaloversee.model.UserPreference
import com.dicoding.medicaloversee.ui.ViewModelFactory
import com.dicoding.medicaloversee.ui.main.MainViewModel
import java.io.IOException
import java.util.Locale

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ProfileFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: FragmentProfileBinding

    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userPreference = UserPreference.getInstance(requireContext().dataStore)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(userPreference)
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(viewLifecycleOwner) { user ->
            if (user.isLogin) {
                binding.profileName.text = user.name
                binding.profileEmail.text = user.email
                binding.profilePhone.text = user.phone
                binding.profileNik.text = user.nik

                val addressName = getAddressName(user.lintang.toDouble(), user.bujur.toDouble())
                binding.profileLocation.text = addressName
            }
        }
    }

    private fun setupAction() {
        binding.btnLogut.setOnClickListener {
            mainViewModel.logout()
        }
    }

    private fun getAddressName(lintang: Double, bujur: Double): String? {
        var addressName: String? = null
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            @Suppress("DEPRECATION")
            val list = geocoder.getFromLocation(lintang, bujur, 1)
            if (list != null && list.size != 0) {
                val address = list[0]
                addressName = "${address.locality}, ${address.adminArea}"
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressName
    }
}