package com.dicoding.medicaloversee.ui.maps

import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dicoding.medicaloversee.R
import com.dicoding.medicaloversee.data.remote.response.Hospital
import com.dicoding.medicaloversee.databinding.FragmentMapsBinding
import com.dicoding.medicaloversee.model.UserPreference
import com.dicoding.medicaloversee.ui.ViewModelFactory
import com.dicoding.medicaloversee.ui.home.HomeViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentMapsBinding

    private lateinit var userPreference: UserPreference
    private var token: String = ""

    //private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userPreference = UserPreference.getInstance(requireContext().dataStore)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isIndoorLevelPickerEnabled = true
            isCompassEnabled = true
            isMapToolbarEnabled = true
        }

        setupViewModel()
        observeViewModel()
        setupStoryMarker()
        //setMapStyle()
    }

    private fun setupViewModel() {
        homeViewModel = ViewModelProvider(
            this,
            ViewModelFactory(userPreference)
        )[HomeViewModel::class.java]
    }

    private fun observeViewModel() {
        homeViewModel.isError.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Failed to load story", Toast.LENGTH_SHORT).show()
        }

        homeViewModel.isSuccess.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        homeViewModel.listHospital.observe(viewLifecycleOwner) {
            addManyMarker(it)
        }
    }

    private fun addManyMarker(listHospital: List<Hospital>) {
        listHospital.forEachIndexed { index, hospital ->
            val latLng = LatLng(hospital.lintang, hospital.bujur)
            val addressName = getAddressName(hospital.lintang, hospital.bujur)
            mMap.addMarker(MarkerOptions().position(latLng).title(hospital.namaRS).snippet("$addressName"))
            //boundsBuilder.include(latLng)

            if (index == 0) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            }
        }

        /*val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )*/
    }

    private fun getAddressName(lat: Double, lon: Double): String? {
        var addressName: String? = null
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            @Suppress("DEPRECATION")
            val list = geocoder.getFromLocation(lat, lon, 1)
            if (list != null && list.size != 0) {
                val address = list[0]
                addressName = "${address.locality}, ${address.adminArea}"
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressName
    }

    private fun setupStoryMarker() {
        userPreference = UserPreference.getInstance(requireContext().dataStore)
        lifecycleScope.launch {
            userPreference.getUser().collect { userModel ->
                token = userModel.token
                homeViewModel.getAllHospitals(token)
            }
        }
    }

    /*private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }*/

    /*companion object {
        const val TAG = "maps"
    }*/
}