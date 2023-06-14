package com.dicoding.medicaloversee.ui.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.medicaloversee.adapter.ListHospitalAdapter
import com.dicoding.medicaloversee.data.remote.response.Hospital
import com.dicoding.medicaloversee.databinding.FragmentHomeBinding
import com.dicoding.medicaloversee.model.UserPreference
import com.dicoding.medicaloversee.ui.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var listHospitalAdapter: ListHospitalAdapter

    private lateinit var userPreference: UserPreference
    private var token: String = ""

    private var originalListHospital: List<Hospital> = emptyList()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userPreference = UserPreference.getInstance(requireContext().dataStore)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        setupViewModel()
        observeViewModel()
        setupAction()
        setupSearchBar()
        setupRecyclerView()
    }

    private fun setupViewModel() {
        homeViewModel = ViewModelProvider(
            this,
            ViewModelFactory(userPreference)
        )[HomeViewModel::class.java]

        homeViewModel.getUser().observe(viewLifecycleOwner) { user ->
            if (user.isLogin) {
                val addressName = getAddressName(user.lintang.toDouble(), user.bujur.toDouble())
                binding.myLocation.text = addressName
            }
        }

        homeViewModel.originalListHospital.observe(viewLifecycleOwner) { hospitals ->
            originalListHospital = hospitals
            listHospitalAdapter.submitList(originalListHospital)
        }
    }

    private fun observeViewModel() {
        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        homeViewModel.isError.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Failed to load story", Toast.LENGTH_SHORT).show()
        }

        homeViewModel.isSuccess.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        homeViewModel.hospitalAdapter.observe(viewLifecycleOwner) { listHospitalAdapter ->
            binding.rvHospitals.adapter = listHospitalAdapter
        }
    }

    private fun setupAction() {

        binding.btnLocation.setOnClickListener {

            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {

                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        val latitude = location.latitude
                        val longitude = location.longitude

                        lifecycleScope.launch {
                            userPreference.updateLocation(latitude.toString(), longitude.toString())
                        }

                        lifecycleScope.launch {
                            userPreference.getUser().collect { userModel ->
                                token = userModel.token
                                homeViewModel.getAllHospitals(token)
                            }
                        }
                    } ?: Toast.makeText(
                        requireContext(),
                        "Lokasi tidak tersedia",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                @Suppress("DEPRECATION")
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun setupSearchBar() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    val searchText = searchView.text?.trim().toString()
                    Toast.makeText(requireActivity(), searchView.text, Toast.LENGTH_SHORT).show()
                    performSearch(searchText)
                    searchView.hide()
                    false
                }
        }
    }

    private fun performSearch(query: String) {
        val filteredList = originalListHospital.filter { hospital ->
            hospital.namaRS.contains(query, ignoreCase = true)
        }
        listHospitalAdapter.submitList(filteredList)
        updateAdapter(listHospitalAdapter)
    }

    private fun updateAdapter(adapter: ListHospitalAdapter) {
        binding.rvHospitals.adapter = adapter
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

    private fun setupRecyclerView() {
        val recylerView = binding.rvHospitals
        recylerView.layoutManager = LinearLayoutManager(requireContext())

        listHospitalAdapter = ListHospitalAdapter()
        recylerView.adapter = listHospitalAdapter

        listHospitalAdapter.submitList(originalListHospital)

        userPreference = UserPreference.getInstance(requireContext().dataStore)
        lifecycleScope.launch {
            userPreference.getUser().collect { userModel ->
                token = userModel.token
                homeViewModel.getAllHospitals(token)
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 10
    }
}